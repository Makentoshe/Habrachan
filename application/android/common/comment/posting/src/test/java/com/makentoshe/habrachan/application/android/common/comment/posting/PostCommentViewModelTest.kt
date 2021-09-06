package com.makentoshe.habrachan.application.android.common.comment.posting

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.makentoshe.habrachan.application.common.arena.comments.PostCommentArena
import com.makentoshe.habrachan.entity.articleId
import com.makentoshe.habrachan.functional.Option
import com.makentoshe.habrachan.functional.Result
import com.makentoshe.habrachan.functional.getOrThrow
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.request.PostCommentRequest
import com.makentoshe.habrachan.network.response.PostCommentResponse
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.model.TestTimedOutException
import org.robolectric.RobolectricTestRunner

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class PostCommentViewModelTest {

    @get:Rule
    internal var instantExecutorRule = InstantTaskExecutorRule()

    private val postCommentSpec = PostCommentSpec(articleId(1), "message")

    private val mockUserSession = mockk<UserSession>()
    private val mockPostCommentArena = mockk<PostCommentArena>()
    private val mockPostCommentRequest = mockk<PostCommentRequest>()
    private val mockPostCommentResponse = mockk<PostCommentResponse>()

    @Test
    fun testShouldReturnModelWithResponseOnSuccessChannel() = runBlocking {
        every {
            mockPostCommentArena.request(mockUserSession, postCommentSpec.articleId, postCommentSpec.message, postCommentSpec.commentId)
        } returns mockPostCommentRequest
        coEvery {
            mockPostCommentArena.suspendCarry(mockPostCommentRequest)
        } returns Result.success(mockPostCommentResponse)

        val viewModel = PostCommentViewModel(mockUserSession, mockPostCommentArena, Option.None)
        launch { viewModel.channel.send(postCommentSpec) }
        val model = viewModel.model.first()

        Assert.assertEquals(mockPostCommentResponse, model.getOrThrow().response)
    }

    @Test
    fun testShouldReturnExceptionOnFailureChannel() = runBlocking {
        val exception = Exception("Stub!")
        every {
            mockPostCommentArena.request(mockUserSession, postCommentSpec.articleId, postCommentSpec.message, postCommentSpec.commentId)
        } returns mockPostCommentRequest
        coEvery {
            mockPostCommentArena.suspendCarry(mockPostCommentRequest)
        } returns Result.failure(exception)

        val viewModel = PostCommentViewModel(mockUserSession, mockPostCommentArena, Option.None)
        launch { viewModel.channel.send(postCommentSpec) }
        val model = viewModel.model.first()

        Assert.assertEquals(exception, model.exceptionOrNull() as Exception)
    }

    @Test(timeout = 500, expected = TestTimedOutException::class) // Job has not completed yet
    fun testShouldNotReturnAModelIfInitialOptionIsNone() = runBlocking {
        PostCommentViewModel(mockUserSession, mockPostCommentArena, Option.None).model.collect()
    }

    @Test
    fun testShouldReturnModelWithResponseOnSuccess() = runBlocking {
        every {
            mockPostCommentArena.request(mockUserSession, postCommentSpec.articleId, postCommentSpec.message, postCommentSpec.commentId)
        } returns mockPostCommentRequest
        coEvery {
            mockPostCommentArena.suspendCarry(mockPostCommentRequest)
        } returns Result.success(mockPostCommentResponse)

        val viewModel = PostCommentViewModel(mockUserSession, mockPostCommentArena, Option.Value(postCommentSpec))
        val model = viewModel.model.first()

        Assert.assertEquals(mockPostCommentResponse, model.getOrThrow().response)
    }

    @Test
    fun testShouldReturnExceptionOnFailure() = runBlocking {
        val exception = Exception("Stub!")
        every {
            mockPostCommentArena.request(mockUserSession, postCommentSpec.articleId, postCommentSpec.message, postCommentSpec.commentId)
        } returns mockPostCommentRequest
        coEvery {
            mockPostCommentArena.suspendCarry(mockPostCommentRequest)
        } returns Result.failure(exception)

        val viewModel = PostCommentViewModel(mockUserSession, mockPostCommentArena, Option.Value(postCommentSpec))
        val model = viewModel.model.first()

        Assert.assertEquals(exception, model.exceptionOrNull() as Exception)
    }

}