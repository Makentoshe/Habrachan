import com.makentoshe.habrachan.entity.CommentVote
import com.makentoshe.habrachan.entity.commentId
import com.makentoshe.habrachan.functional.getOrThrow
import com.makentoshe.habrachan.network.NativeVoteCommentDeserializer
import com.makentoshe.habrachan.network.NativeVoteCommentException
import com.makentoshe.habrachan.network.NativeVoteCommentManager
import com.makentoshe.habrachan.network.api.NativeCommentsApi
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.junit.Test
import retrofit2.Call
import retrofit2.Response

class NativeVoteCommentManagerTest : UnitTest() {

    private val okHttpClient = spyk(OkHttpClient())
    private val mockNativeCommentsApi = mockk<NativeCommentsApi>()

    @Test
    fun testShouldCheckProperRequestParams() {
        val manager = NativeVoteCommentManager.Builder(okHttpClient).build()
        val request = manager.request(userSession, commentId(-1), CommentVote.Down)

        assertEquals(CommentVote.Down, request.commentVote)
        assertEquals(-1, request.commentId)
        assertEquals(userSession, request.userSession)
    }

    @Test
    fun testShouldCheckSuccessfulVotingUp() = runBlocking {
        val manager = NativeVoteCommentManager(mockNativeCommentsApi, NativeVoteCommentDeserializer())
        val request = manager.request(userSession, commentId(-1), CommentVote.Up)

        every {
            mockNativeCommentsApi.voteUp(request.userSession.client, request.userSession.token, request.commentId.commentId)
        } returns mockedCallResponse {
            mockedResponse {
                mockedResponseBody(getResourceString("vote_comment_success.json"))
            }
        }

        val response = manager.vote(request).getOrThrow()
        assertEquals(7, response.score)
        assertEquals(-1, response.request.commentId)
        assertEquals(CommentVote.Up, response.request.commentVote)
    }

    @Test
    fun testShouldCheckFailureVotingDown() = runBlocking {
        val manager = NativeVoteCommentManager(mockNativeCommentsApi, NativeVoteCommentDeserializer())
        val request = manager.request(userSession, commentId(-1), CommentVote.Down)

        every {
            mockNativeCommentsApi.voteDown(request.userSession.client, request.userSession.token, request.commentId.commentId)
        } returns mockedCallResponse {
            mockedResponse(isSuccessful = false, code = 400) {
                mockedResponseBody(getResourceString("vote_comment_failure.json"))
            }
        }

        val response = manager.vote(request).exceptionOrNull() as NativeVoteCommentException
        assertEquals("Incorrect parameters", response.message)
        assertEquals(400, response.code)
        assertEquals("Повторное голосование запрещено", response.additional.first())
    }

    @Test
    fun testShouldCheckApiFailure() = runBlocking {
        val networkFailureMessage = "Network exception in a nutshell"
        val networkFailureException = Exception(networkFailureMessage)

        val manager = NativeVoteCommentManager(mockNativeCommentsApi, NativeVoteCommentDeserializer())
        val request = manager.request(userSession, commentId(-1), CommentVote.Down)

        every {
            mockNativeCommentsApi.voteDown(request.userSession.client, request.userSession.token, request.commentId.commentId)
        } throws networkFailureException

        val response = manager.vote(request).exceptionOrNull() as NativeVoteCommentException
        assertEquals(networkFailureMessage, response.additional.first())
        assertEquals(networkFailureException, response.cause)
        assertEquals(request, response.request)
        assertEquals("", response.raw)
        assertEquals(0, response.code)
        assertEquals(networkFailureMessage, response.message)
    }

    private fun mockedResponseBody(string: String): ResponseBody {
        val mockResponseBody = mockk<ResponseBody>()
        every { mockResponseBody.string() } returns string
        return mockResponseBody
    }

    private fun mockedResponse(
        isSuccessful: Boolean = true,
        code: Int = 200,
        message: String = "Mocked message",
        responseBodyScope: () -> ResponseBody
    ): Response<ResponseBody> {
        val mockResponse = mockk<Response<ResponseBody>>()
        every { mockResponse.isSuccessful } returns isSuccessful
        every { mockResponse.code() } returns code
        every { mockResponse.message() } returns message

        if (isSuccessful) {
            every { mockResponse.body() } returns responseBodyScope()
        } else {
            every { mockResponse.errorBody() } returns responseBodyScope()
        }

        return mockResponse
    }

    private fun mockedCallResponse(mockResponseScope: () -> Response<ResponseBody>): Call<ResponseBody> {
        val mockCallResponseBody = mockk<Call<ResponseBody>>()
        every { mockCallResponseBody.execute() } returns mockResponseScope()
        return mockCallResponseBody
    }

}