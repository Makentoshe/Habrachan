import com.makentoshe.habrachan.api.AdditionalRequestParameters
import com.makentoshe.habrachan.entity.user.component.UserLogin
import com.makentoshe.habrachan.entity.user.get.avatar
import com.makentoshe.habrachan.entity.user.get.login
import com.makentoshe.habrachan.functional.Either2
import com.makentoshe.habrachan.functional.Option2
import com.makentoshe.habrachan.network.user.get.GetUserManager
import com.makentoshe.habrachan.network.user.get.GetUserRequest
import com.makentoshe.habrachan.network.user.get.networkCode
import com.makentoshe.habrachan.network.user.get.networkMessage
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.nio.channels.UnresolvedAddressException

class GetUserManagerTest {

    @Test
    fun `test should check response on success networking`() = runBlocking {
        val json = javaClass.classLoader.getResourceAsStream("get_user_success.json").readAllBytes().decodeToString()
        val request = GetUserRequest(UserLogin("Makentoshe"), AdditionalRequestParameters())

        val response = requireManagerWithJson(json, HttpStatusCode.OK).execute(request) as Either2.Left

        assertEquals(request, response.value.request)
        assertEquals("Makentoshe", response.value.user.login.value.string)
        assertEquals(Option2.None, response.value.user.avatar)
    }

    @Test
    fun `test should check response on failure network`() = runBlocking {
        val request = GetUserRequest(UserLogin("Makentoshe"), AdditionalRequestParameters())
        val response = requireManagerWithException(UnresolvedAddressException()).execute(request) as Either2.Right

        assertEquals(request, response.value.request)
        assert(response.value.cause is UnresolvedAddressException)
        assertNull(response.value.networkCode.getOrNull())
        assertNull(response.value.networkMessage.getOrNull())
    }



    private fun requireManagerWithJson(json: String, status: HttpStatusCode): GetUserManager {
        return requireManagerWithNetwork {
            respond(ByteReadChannel(json), status, headersOf(HttpHeaders.ContentType, "application/json"))
        }
    }

    private fun requireManagerWithNetwork(
        action: MockRequestHandleScope.(HttpRequestData) -> HttpResponseData
    ): GetUserManager {
        return GetUserManager(HttpClient(MockEngine { request -> action(request) }))
    }

    private fun requireManagerWithException(exception: Exception): GetUserManager {
        return GetUserManager(HttpClient(MockEngine { request -> throw exception }))
    }
}