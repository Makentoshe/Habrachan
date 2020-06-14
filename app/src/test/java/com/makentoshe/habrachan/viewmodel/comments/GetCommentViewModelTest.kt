package com.makentoshe.habrachan.viewmodel.comments

import com.makentoshe.habrachan.BaseTest
import com.makentoshe.habrachan.common.database.CacheDatabase
import com.makentoshe.habrachan.common.database.session.SessionDatabase
import com.makentoshe.habrachan.common.network.manager.CommentsManager
import com.makentoshe.habrachan.common.network.response.GetCommentsResponse
import com.makentoshe.habrachan.viewmodel.NetworkSchedulerProvider
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test

@Suppress("ReactiveStreamsUnusedPublisher")
class GetCommentViewModelTest : BaseTest() {

    private lateinit var viewModel: GetCommentViewModel

    private val spyCompositeDisposable = spyk(CompositeDisposable())
    private val mockCommentsManager = mockk<CommentsManager>(relaxed = true)
    private val mockCacheDatabase = mockk<CacheDatabase>(relaxed = true)
    private val mockSessionDatabase = mockk<SessionDatabase>(relaxed = true)
    private val schedulerProvider = object : NetworkSchedulerProvider {
        override val networkScheduler = Schedulers.trampoline()
    }

    @Before
    fun before() {
        viewModel = GetCommentViewModel.Factory(
            schedulerProvider, spyCompositeDisposable, mockCommentsManager, mockCacheDatabase, mockSessionDatabase
        ).buildViewModelAttachedTo(mockk(relaxed = true))
    }

    @Test
    fun testShouldReturnGetCommentsResponseOnRequest() {
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