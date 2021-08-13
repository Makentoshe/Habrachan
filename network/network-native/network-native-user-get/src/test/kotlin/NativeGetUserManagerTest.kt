import com.makentoshe.habrachan.functional.getOrThrow
import com.makentoshe.habrachan.network.NativeGetUserDeserializer
import com.makentoshe.habrachan.network.NativeGetUserException
import com.makentoshe.habrachan.network.NativeGetUserManager
import com.makentoshe.habrachan.network.api.NativeUsersApi
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import javax.net.ssl.SSLHandshakeException

class NativeGetUserManagerTest : RetrofitUnitTest() {

    private val mockUsersApi = mockk<NativeUsersApi>()
    private val manager = NativeGetUserManager(mockUsersApi, NativeGetUserDeserializer())

    @Test
    fun testManagerShouldReturnUserOnSuccess() = runBlocking {
        every {
            mockUsersApi.getUser(any(), any(), any(), any(), any())
        } returns mockedCallResponse {
            mockedResponse(isSuccessful = true, code = 200) {
                mockedResponseBody(getResourceString("get_user_success.json"))
            }
        }

        val request = manager.request(userSession, "makentoshe")
        val response = manager.user(request).getOrThrow()

        assertEquals(request, response.request)
        assertEquals("Makentoshe", response.user.login)
        assertEquals(1961555, response.user.userId)
    }

    @Test
    fun testManagerShouldReturnExceptionOnNetworkFailure() = runBlocking {
        val exception = SSLHandshakeException("Exception for testing purposes")
        every {
            mockUsersApi.getUser(any(), any(), any(), any(), any())
        } throws exception

        val request = manager.request(userSession, "makentoshe")
        val response = manager.user(request).exceptionOrNull() as NativeGetUserException

        assertEquals(request, response.request)
        assertEquals(exception, response.cause)
    }

    @Test
    fun testManagerShouldReturnExceptionOnDeserializeFailure() = runBlocking {
        val json = "{ anotehr one malformed json }"
        every {
            mockUsersApi.getUser(any(), any(), any(), any(), any())
        } returns mockedCallResponse {
            mockedResponse(isSuccessful = true, code = 200) {
                mockedResponseBody(json)
            }
        }
        val request = manager.request(userSession, "makentoshe")
        val response = manager.user(request).exceptionOrNull() as NativeGetUserException

        assertEquals(request, response.request)
        assertEquals(json, response.raw)
    }

    @Test
    fun testManagerShouldReturnExceptionOnCredentialsFailure() = runBlocking {
        val json = getResourceString("get_user_failure.json")
        every {
            mockUsersApi.getUser(any(), any(), any(), any(), any())
        } returns mockedCallResponse {
            mockedResponse(isSuccessful = false, code = 401) {
                mockedResponseBody(json)
            }
        }

        val request = manager.request(userSession, "makentoshe")
        val response = manager.user(request).exceptionOrNull() as NativeGetUserException

        assertEquals(request, response.request)
        assertEquals(json, response.raw)
    }
}