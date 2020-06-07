package com.makentoshe.habrachan.viewmodel.comments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.makentoshe.habrachan.common.database.CacheDatabase
import com.makentoshe.habrachan.common.database.session.SessionDatabase
import com.makentoshe.habrachan.common.entity.comment.Comment
import com.makentoshe.habrachan.common.network.manager.CommentsManager
import com.makentoshe.habrachan.common.network.manager.ImageManager
import com.makentoshe.habrachan.common.network.response.GetCommentsResponse
import com.makentoshe.habrachan.common.network.response.ImageResponse
import com.makentoshe.habrachan.common.network.response.VoteCommentResponse
import io.mockk.*
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@Suppress("ReactiveStreamsUnusedPublisher")
class CommentsFragmentViewModelTest {

    private lateinit var viewModel: CommentsFragmentViewModel

    private val mockCommentsManager = mockk<CommentsManager>(relaxed = true)
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
    fun testShouldReturnErrorResponseOnVoteDownException() {
        val testObservable = viewModel.voteDownCommentObservable.test()

        every { mockCommentsManager.voteDown(any()) } returns Single.just(Exception()).map { throw it }

        val commentId = 39
        viewModel.voteDownCommentObserver.onNext(commentId)

        testObservable.assertValue { it is VoteCommentResponse.Error }.dispose()
    }

    @Test
    fun testShouldReturnStubAvatarWithoutNetworkAndCache() {
        val response = viewModel.getAvatarObservable("stub-user").blockingFirst()
        assert((response as ImageResponse.Success).isStub)

        verify(exactly = 0) { mockImageManager.getImage(any()) }
        verify(exactly = 0) { mockCacheDatabase.avatars().get(any()) }
    }

    @Test
    fun testShouldReturnSuccessImageResponseFromCache() {
        val mockBitmap = mockk<Bitmap>(relaxed = true)
        every { mockCacheDatabase.avatars().get(any()) } returns mockBitmap

        val response = viewModel.getAvatarObservable("from cache").blockingFirst()
        assert(response is ImageResponse.Success)

        verify { mockCacheDatabase.avatars().get(any()) }
        verify(exactly = 0) { mockImageManager.getImage(any()) }
    }

    @Test
    fun testShouldReturnSuccessImageResponseOnAvatarRequest() {
        mockkStatic(BitmapFactory::class)
        every { BitmapFactory.decodeByteArray(any(), any(), any()) } returns mockk(relaxed = true)

        every { mockCacheDatabase.avatars().get(any()) } returns null

        val mockImageResponse = mockk<ImageResponse.Success>(relaxed = true)
        every { mockImageManager.getImage(any()) } returns Single.just(mockImageResponse)

        val response = viewModel.getAvatarObservable("from network").blockingFirst()
        assertEquals(response, mockImageResponse)

        unmockkStatic(BitmapFactory::class)
    }

    @Test
    fun testShouldSaveSuccessImageResponseToCache() {
        mockkStatic(BitmapFactory::class)
        every { BitmapFactory.decodeByteArray(any(), any(), any()) } returns mockk(relaxed = true)
        every { mockImageManager.getImage(any()) } returns Single.just(mockk<ImageResponse.Success>(relaxed = true))
        every { mockCacheDatabase.avatars().get("from network") } returns null

        viewModel.getAvatarObservable("from network").blockingFirst()

        verify { mockCacheDatabase.avatars().insert("from network", any()) }

        unmockkStatic(BitmapFactory::class)
    }

    @Test
    fun testShouldReturnErrorImageResponseFromNetwork() {
        every { mockImageManager.getImage(any()) } returns Single.just(Exception("exception")).map { throw it }
        every { mockCacheDatabase.avatars().get("from network") } returns null

        val response = viewModel.getAvatarObservable("from network").blockingFirst()
        assert(response is ImageResponse.Error)
    }

    @Test
    fun testShouldReturnErrorImageResponseOnNetworkError() {
        every { mockCacheDatabase.avatars().get(any()) } throws Exception("something wrong")

        val response = viewModel.getAvatarObservable("").blockingFirst()
        assert(response is ImageResponse.Error)
    }
}