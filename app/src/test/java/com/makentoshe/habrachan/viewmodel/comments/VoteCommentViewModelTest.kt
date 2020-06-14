package com.makentoshe.habrachan.viewmodel.comments

import com.makentoshe.habrachan.BaseTest
import com.makentoshe.habrachan.common.database.CacheDatabase
import com.makentoshe.habrachan.common.database.session.SessionDatabase
import com.makentoshe.habrachan.common.entity.comment.Comment
import com.makentoshe.habrachan.common.network.manager.CommentsManager
import com.makentoshe.habrachan.common.network.response.VoteCommentResponse
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
class VoteCommentViewModelTest : BaseTest() {

    private lateinit var viewModel: VoteCommentViewModel

    private val spyCompositeDisposable = spyk(CompositeDisposable())
    private val mockCommentsManager = mockk<CommentsManager>(relaxed = true)
    private val mockCacheDatabase = mockk<CacheDatabase>(relaxed = true)
    private val mockSessionDatabase = mockk<SessionDatabase>(relaxed = true)
    private val schedulerProvider = object : NetworkSchedulerProvider {
        override val networkScheduler = Schedulers.trampoline()
    }

    @Before
    fun before() {
        viewModel = VoteCommentViewModel.Factory(
            schedulerProvider, spyCompositeDisposable, mockCommentsManager, mockCacheDatabase, mockSessionDatabase
        ).buildViewModelAttachedTo(mockk(relaxed = true))
    }

    @Test
    fun testShouldReplaceCommentInDatabaseOnSuccessUpVote() {
        val mockComment = mockk<Comment>(relaxed = true)
        every { mockCacheDatabase.comments().getById(any()) } returns mockComment

        val mockResponse = mockk<VoteCommentResponse.Success>(relaxed = true)
        every { mockResponse.score } returns 10
        every { mockCommentsManager.voteUp(any()) } returns Single.just(mockResponse)

        val commentId = 39
        viewModel.voteUpCommentObserver.onNext(commentId)

        verify { mockCacheDatabase.comments().insert(any()) }
        verify { mockComment.copy(score = 10) }
    }

    @Test
    fun testShouldReturnErrorVoteUpCommentResponseWithoutCaching() {
        val mockComment = mockk<Comment>(relaxed = true)
        every { mockCacheDatabase.comments().getById(any()) } returns mockComment

        val mockResponse = mockk<VoteCommentResponse.Error>(relaxed = true)
        every { mockCommentsManager.voteUp(any()) } returns Single.just(mockResponse)

        val commentId = 39
        viewModel.voteUpCommentObserver.onNext(commentId)

        verify(exactly = 0) { mockCacheDatabase.comments().insert(any()) }
    }

    @Test
    fun testShouldResponseOnVoteUpRequest() {
        val testSubject = viewModel.voteUpCommentObservable.test()

        val mockComment = mockk<Comment>(relaxed = true)
        every { mockCacheDatabase.comments().getById(any()) } returns mockComment

        val mockResponse = mockk<VoteCommentResponse.Success>(relaxed = true)
        every { mockCommentsManager.voteUp(any()) } returns Single.just(mockResponse)

        val commentId = 39
        viewModel.voteUpCommentObserver.onNext(commentId)

        testSubject.assertValues(mockResponse).dispose()
    }

    @Test
    fun testShouldResponseOnVoteDownRequest() {
        val testSubject = viewModel.voteDownCommentObservable.test()

        val mockComment = mockk<Comment>(relaxed = true)
        every { mockCacheDatabase.comments().getById(any()) } returns mockComment

        val mockResponse = mockk<VoteCommentResponse.Error>(relaxed = true)
        every { mockCommentsManager.voteDown(any()) } returns Single.just(mockResponse)

        val commentId = 39
        viewModel.voteDownCommentObserver.onNext(commentId)

        testSubject.assertValues(mockResponse).dispose()
    }

    @Test
    fun testShouldReturnErrorResponseOnVoteUpException() {
        val testObservable = viewModel.voteUpCommentObservable.test()

        every { mockCommentsManager.voteUp(any()) } returns Single.just(Exception()).map { throw it }

        val commentId = 39
        viewModel.voteUpCommentObserver.onNext(commentId)

        testObservable.assertValue { it is VoteCommentResponse.Error }.dispose()
    }

    @Test
    fun testShouldReplaceCommentInDatabaseOnSuccessDownVote() {
        val mockComment = mockk<Comment>(relaxed = true)
        every { mockCacheDatabase.comments().getById(any()) } returns mockComment

        val mockResponse = mockk<VoteCommentResponse.Success>(relaxed = true)
        every { mockResponse.score } returns 10
        every { mockCommentsManager.voteDown(any()) } returns Single.just(mockResponse)

        val commentId = 39
        viewModel.voteDownCommentObserver.onNext(commentId)

        verify { mockCacheDatabase.comments().insert(any()) }
        verify { mockComment.copy(score = 10) }
    }

    @Test
    fun testShouldReturnErrorResponseOnVoteDownException() {
        val testObservable = viewModel.voteDownCommentObservable.test()

        every { mockCommentsManager.voteDown(any()) } returns Single.just(Exception()).map { throw it }

        val commentId = 39
        viewModel.voteDownCommentObserver.onNext(commentId)

        testObservable.assertValue { it is VoteCommentResponse.Error }.dispose()
    }
}