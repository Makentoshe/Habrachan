package com.makentoshe.habrachan.viewmodel.main.articles

import com.makentoshe.habrachan.BaseTest
import com.makentoshe.habrachan.common.database.*
import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.common.entity.post.ArticlesResponse
import com.makentoshe.habrachan.common.network.manager.HabrArticleManager
import io.mockk.*
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.lang.Exception
import java.lang.RuntimeException

class ArticlesViewModelTest : BaseTest() {

    private lateinit var viewModel: ArticlesViewModel
    private val sessionDao = mockk<SessionDao>()
    private val articleManager = mockk<HabrArticleManager>()
    private val articleDao = mockk<ArticleDao>()
    private val commentDao = mockk<CommentDao>()
    private val avatarDao = mockk<AvatarDao>()
    private val userDao = mockk<UserDao>()

    @Before
    fun before() {
        every { sessionDao.get() } returns session
        every { sessionDao.clear() } just runs

        every { articleDao.clear() } just runs
        every { articleDao.insert(any()) } just runs

        every { commentDao.clear() } just runs

        every { avatarDao.clear() } just runs

        every { userDao.clear() } just runs
        every { userDao.insert(any()) } just runs

        viewModel = ArticlesViewModel(sessionDao, articleManager, articleDao, commentDao, avatarDao, userDao)
    }

    @Test
    fun testShouldCreateRequestAll() {
        val page = 1
        val request = viewModel.createRequestAll(page)

        assertEquals(session.clientKey, request.client)
        assertEquals(session.apiKey, request.api)
        assertEquals(session.tokenKey, request.token)
        assertEquals(page, request.page)
        assertEquals("posts/all", request.spec)
    }


    @Test
    fun testShouldAddSuccessResultToCache() {
        val mockArticles = Array(20) { mockk<Article>().also { mock -> every { mock.author } returns mockk() } }.toList()
        val mockArticlesResponse = mockk<ArticlesResponse.Success>()
        every { articleManager.getArticles(any()) } returns Single.just(mockArticlesResponse)
        every { mockArticlesResponse.data } returns mockArticles

        val request = viewModel.createRequestAll(10)
        viewModel.requestObserver.onNext(request)
        val response = viewModel.articlesObservable.blockingFirst() as ArticlesResponse.Success

        verify(exactly = 20) { userDao.insert(any()) }
        verify(exactly = 20) { articleDao.insert(any()) }

        assertEquals(mockArticlesResponse, response)
    }

    @Test
    fun testShouldClearAllCachesForNewRequest() {
        val mockArticles = Array(20) { mockk<Article>().also { mock -> every { mock.author } returns mockk() } }.toList()
        val mockArticlesResponse = mockk<ArticlesResponse.Success>()
        every { articleManager.getArticles(any()) } returns Single.just(mockArticlesResponse)
        every { mockArticlesResponse.data } returns mockArticles

        val request = viewModel.createRequestAll(1)
        viewModel.requestObserver.onNext(request)
        val response = viewModel.articlesObservable.blockingFirst() as ArticlesResponse.Success

        verify { userDao.clear() }
        verify { articleDao.clear() }
        verify { commentDao.clear() }
        verify { avatarDao.clear() }

        assertEquals(mockArticlesResponse, response)
    }

    @Test
    fun testShouldReturnErrorResponse() {
        val exception = RuntimeException("Message")
        every { articleManager.getArticles(any()) } returns Single.just(Unit).map { throw exception }

        val request = viewModel.createRequestAll(2)
        viewModel.requestObserver.onNext(request)
        val response = viewModel.articlesObservable.blockingFirst() as ArticlesResponse.Error

        assertEquals("java.lang.RuntimeException: Message", response.json)
    }

    @Test
    fun testShouldReturnResultFromCache() {
        every { articleManager.getArticles(any()) } returns Single.just(Unit).map { throw Exception() }
        val mockArticles = Array(20) { mockk<Article>().also { mock -> every { mock.author } returns mockk() } }.toList()
        every { articleDao.getAll() } returns mockArticles

        val request = viewModel.createRequestAll(1)
        viewModel.requestObserver.onNext(request)
        val response = viewModel.articlesObservable.blockingFirst() as ArticlesResponse.Success

        assertEquals(mockArticles, response.data)
    }

    @Test
    fun testShouldReturnErrorResultFromCache() {
        val exception = RuntimeException("Message")
        every { articleManager.getArticles(any()) } returns Single.just(Unit).map { throw exception }
        every { articleDao.getAll() } returns listOf()

        val request = viewModel.createRequestAll(1)
        viewModel.requestObserver.onNext(request)
        val response = viewModel.articlesObservable.blockingFirst() as ArticlesResponse.Error

        assertEquals("java.lang.RuntimeException: Message", response.json)
    }
}