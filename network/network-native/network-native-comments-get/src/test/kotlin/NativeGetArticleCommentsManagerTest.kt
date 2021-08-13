import com.makentoshe.habrachan.functional.getOrThrow
import com.makentoshe.habrachan.network.NativeGetArticleCommentsDeserializer
import com.makentoshe.habrachan.network.NativeGetArticleCommentsException
import com.makentoshe.habrachan.network.NativeGetArticleCommentsManager
import com.makentoshe.habrachan.network.api.NativeCommentsApi
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import javax.net.ssl.SSLHandshakeException

class NativeGetArticleCommentsManagerTest : RetrofitUnitTest() {

    private val mockCommentsApi = mockk<NativeCommentsApi>()
    private val manager = NativeGetArticleCommentsManager(mockCommentsApi, NativeGetArticleCommentsDeserializer())

    @Test
    fun testManagerShouldReturnListOfCommentsOnSuccess() = runBlocking {
        every {
            mockCommentsApi.getComments(any(), any(), any(), any(), any())
        } returns mockedCallResponse {
            mockedResponse {
                mockedResponseBody(getResourceString("get_article_comments_success.json"))
            }
        }

        val request = manager.request(userSession, 0)
        val response = manager.comments(request).getOrThrow()

        assertEquals(request, response.request)
        assertEquals(26, response.data.size)
    }

    @Test
    fun testManagerShouldReturnExceptionOnNetworkError() = runBlocking {
        val exception = SSLHandshakeException("Just for test purposes")
        every {
            mockCommentsApi.getComments(any(), any(), any(), any(), any())
        } throws exception

        val request = manager.request(userSession, 0)
        val response = manager.comments(request).exceptionOrNull() as NativeGetArticleCommentsException

        assertEquals(request, response.request)
        assertEquals(exception, response.cause)
    }

    @Test
    fun testManagerShouldReturnExceptionOnDeserializeFailure() = runBlocking {
        val json = "{malformed json}"
        every {
            mockCommentsApi.getComments(any(), any(), any(), any(), any())
        } returns mockedCallResponse {
            mockedResponse {
                mockedResponseBody(json)
            }
        }

        val request = manager.request(userSession, 0)
        val response = manager.comments(request).exceptionOrNull() as NativeGetArticleCommentsException

        assertEquals(request, response.request)
        assertEquals(json, response.raw)
    }

    @Test
    fun testManagerShouldReturnExceptionOnUserCredentialsError() = runBlocking {
        val json = getResourceString("get_article_comments_failure.json")
        every {
            mockCommentsApi.getComments(any(), any(), any(), any(), any())
        } returns mockedCallResponse {
            mockedResponse(isSuccessful = false, code = 401) {
                mockedResponseBody(json)
            }
        }

        val request = manager.request(userSession, 0)
        val response = manager.comments(request).exceptionOrNull() as NativeGetArticleCommentsException

        assertEquals(request, response.request)
        assertEquals(json, response.raw)
    }
}