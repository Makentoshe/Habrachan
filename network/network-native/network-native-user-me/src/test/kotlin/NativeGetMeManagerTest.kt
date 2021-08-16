import com.makentoshe.habrachan.functional.getOrThrow
import com.makentoshe.habrachan.network.NativeGetMeDeserializer
import com.makentoshe.habrachan.network.NativeGetMeException
import com.makentoshe.habrachan.network.NativeGetMeManager
import com.makentoshe.habrachan.network.natives.api.NativeUsersApi
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import javax.net.ssl.SSLHandshakeException

class NativeGetMeManagerTest : RetrofitUnitTest() {

    private val mockUsersApi = mockk<NativeUsersApi>()
    private val manager = NativeGetMeManager(mockUsersApi, NativeGetMeDeserializer())

    @Test
    fun testManagerShouldReturnUserOnSuccess() = runBlocking {
        every {
            mockUsersApi.getMe(any(), any(), any(), any())
        } returns mockedCallResponse {
            mockedResponse {
                mockedResponseBody(getResourceString("me_user_success.json"))
            }
        }

        val request = manager.request(userSession)
        val response = manager.me(request).getOrThrow()

        assertEquals(request, response.request)
        assertEquals("Makentoshe", response.user.login)
        assertEquals(1961555, response.user.userId)
    }

    @Test
    fun testManagerShouldReturnExceptionOnNetworkFailure() = runBlocking {
        val exception = SSLHandshakeException("Exception for testing purposes")
        every {
            mockUsersApi.getMe(any(), any(), any(), any())
        } throws exception

        val request = manager.request(userSession)
        val response = manager.me(request).exceptionOrNull() as NativeGetMeException

        assertEquals(request, response.request)
        assertEquals(exception, response.cause)
    }

    @Test
    fun testManagerShouldReturnExceptionOnDeserializeFailure() = runBlocking {
        val json = "{ anotehr one malformed json }"
        every {
            mockUsersApi.getMe(any(), any(), any(), any())
        } returns mockedCallResponse {
            mockedResponse(isSuccessful = true, code = 200) {
                mockedResponseBody(json)
            }
        }
        val request = manager.request(userSession)
        val response = manager.me(request).exceptionOrNull() as NativeGetMeException

        assertEquals(request, response.request)
        assertEquals(json, response.raw)
    }

    @Test
    fun testManagerShouldReturnExceptionOnCredentialsFailure() = runBlocking {
        val json = getResourceString("me_user_failure.json")
        every {
            mockUsersApi.getMe(any(), any(), any(), any())
        } returns mockedCallResponse {
            mockedResponse(isSuccessful = false, code = 401) {
                mockedResponseBody(json)
            }
        }

        val request = manager.request(userSession)
        val response = manager.me(request).exceptionOrNull() as NativeGetMeException

        assertEquals(request, response.request)
        assertEquals(json, response.raw)
    }
}