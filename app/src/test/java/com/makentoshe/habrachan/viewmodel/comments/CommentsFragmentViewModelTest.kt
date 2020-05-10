package com.makentoshe.habrachan.viewmodel.comments

import com.makentoshe.habrachan.common.database.CacheDatabase
import com.makentoshe.habrachan.common.database.session.SessionDatabase
import com.makentoshe.habrachan.common.network.manager.HabrCommentsManager
import com.makentoshe.habrachan.common.network.manager.ImageManager
import com.makentoshe.habrachan.common.network.response.GetCommentsResponse
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test

@Suppress("ReactiveStreamsUnusedPublisher")
class CommentsFragmentViewModelTest {

    private lateinit var viewModel: CommentsFragmentViewModel

    private val mockCommentsManager = mockk<HabrCommentsManager>(relaxed = true)
    private val mockImageManager = mockk<ImageManager>(relaxed = true)
    private val mockCacheDatabase = mockk<CacheDatabase>(relaxed = true)
    private val mockSessionDatabase = mockk<SessionDatabase>(relaxed = true)
    private val schedulerProvider = object : CommentsViewModelSchedulerProvider {
        override val networkScheduler = Schedulers.trampoline()
    }

    @Before
    fun before() {
        viewModel = CommentsFragmentViewModel(
            mockCommentsManager, mockImageManager, mockCacheDatabase, mockSessionDatabase, schedulerProvider
        )
    }

    @Test
    fun testShouldReturnResponseOnRequest() {
        val mockResponse = mockk<GetCommentsResponse>(relaxed = true)
        every { mockCommentsManager.getComments(any()) } returns Single.just(mockResponse)

        val articleId = 39
        viewModel.getCommentsObserver.onNext(articleId)

        viewModel.getCommentsObservable.test().assertValue { it == mockResponse }.dispose()
    }

    @Test
    fun testShouldSaveSuccessGetCommentsResponse() {
        val mockResponse = mockk<GetCommentsResponse.Success>(relaxed = true)
        every { mockResponse.data } returns listOf(mockk(relaxed = true))
        every { mockCommentsManager.getComments(any()) } returns Single.just(mockResponse)

        val articleId = 39
        viewModel.getCommentsObserver.onNext(articleId)

        verify { mockCacheDatabase.comments().insert(any()) }
    }

    @Test
    fun testShouldReturnErrorGetCommentsResponseWithoutCaching() {
        val mockResponse = mockk<GetCommentsResponse.Error>(relaxed = true)
        every { mockCommentsManager.getComments(any()) } returns Single.just(mockResponse)

        val articleId = 39
        viewModel.getCommentsObserver.onNext(articleId)

        verify(exactly = 0) { mockCacheDatabase.comments().insert(any()) }
    }

    @Test
    fun testShouldReturnSuccessGetCommentsResponseFromCache() {
        every { mockCommentsManager.getComments(any()) } returns Single.just(Unit).map { throw Exception() }

        val articleId = 39
        viewModel.getCommentsObserver.onNext(articleId)

        verify { mockCacheDatabase.comments().getByArticleId(articleId) }
    }

}