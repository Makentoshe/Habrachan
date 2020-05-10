package com.makentoshe.habrachan.viewmodel.comments

import com.google.android.material.appbar.AppBarLayout
import com.makentoshe.habrachan.common.database.CacheDatabase
import com.makentoshe.habrachan.common.database.session.SessionDatabase
import com.makentoshe.habrachan.common.entity.comment.Comment
import com.makentoshe.habrachan.common.network.manager.HabrCommentsManager
import com.makentoshe.habrachan.common.network.manager.ImageManager
import com.makentoshe.habrachan.common.network.response.GetCommentsResponse
import com.makentoshe.habrachan.common.network.response.VoteCommentResponse
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
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
    fun sas() {
        val testObservable = viewModel.voteDownCommentObservable.test()

        every { mockCommentsManager.voteDown(any()) } returns Single.just(Exception()).map { throw it }

        val commentId = 39
        viewModel.voteDownCommentObserver.onNext(commentId)

        testObservable.assertValue { it is VoteCommentResponse.Error }.dispose()
    }
}