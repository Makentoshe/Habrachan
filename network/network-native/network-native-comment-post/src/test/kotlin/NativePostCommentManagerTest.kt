import com.makentoshe.habrachan.entity.articleId
import com.makentoshe.habrachan.functional.getOrThrow
import com.makentoshe.habrachan.network.NativePostCommentDeserializer
import com.makentoshe.habrachan.network.NativePostCommentException
import com.makentoshe.habrachan.network.NativePostCommentManager
import com.makentoshe.habrachan.network.natives.api.NativeCommentsApi
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import javax.net.ssl.SSLHandshakeException

class NativePostCommentManagerTest : RetrofitUnitTest() {

    private val mockCommentsApi = mockk<NativeCommentsApi>()
    private val manager = NativePostCommentManager(mockCommentsApi, NativePostCommentDeserializer())

    @Test
    fun testManagerShouldReturnPostedCommentOnSuccess() = runBlocking {
        every {
            mockCommentsApi.postComment(any(), any(), any(), any(), any())
        } returns mockedCallResponse {
            mockedResponse {
                mockedResponseBody(getResourceString("post_comment_success.json"))
            }
        }

        val request = manager.request(userSession, articleId(0), "message")
        val response = manager.post(request).getOrThrow()

        assertEquals(request, response.request)
        assertEquals(393939, response.comment.commentId)
        assertEquals("Test message body на двух языках.", response.comment.message)
    }

    @Test
    fun testManagerShouldReturnExceptionOnNetworkError() = runBlocking {
        val exception = SSLHandshakeException("Just for test purposes")
        every {
            mockCommentsApi.postComment(any(), any(), any(), any(), any())
        } throws exception

        val request = manager.request(userSession, articleId(0), "message")
        val response = manager.post(request).exceptionOrNull() as NativePostCommentException

        assertEquals(request, response.request)
        assertEquals(exception, response.cause)
    }

    @Test
    fun testManagerShouldReturnExceptionOnDeserializeFailure() = runBlocking {
        val json = "{malformed json}"
        every {
            mockCommentsApi.postComment(any(), any(), any(), any(), any())
        } returns mockedCallResponse {
            mockedResponse {
                mockedResponseBody(json)
            }
        }

        val request = manager.request(userSession, articleId(0), "message")
        val response = manager.post(request).exceptionOrNull() as NativePostCommentException

        assertEquals(request, response.request)
        assertEquals(json, response.raw)
    }

    @Test
    fun testManagerShouldReturnExceptionOnUserCredentialsError() = runBlocking {
        val json = getResourceString("post_comment_failure.json")
        every {
            mockCommentsApi.postComment(any(), any(), any(), any(), any())
        } returns mockedCallResponse {
            mockedResponse(isSuccessful = false, code = 401) {
                mockedResponseBody(json)
            }
        }

        val request = manager.request(userSession, articleId(0), "message")
        val response = manager.post(request).exceptionOrNull() as NativePostCommentException

        assertEquals(request, response.request)
        assertEquals(json, response.raw)
    }
}
