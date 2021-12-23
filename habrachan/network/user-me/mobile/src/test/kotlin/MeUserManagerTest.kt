import com.makentoshe.habrachan.api.AdditionalRequestParameters
import com.makentoshe.habrachan.entity.me.mobile.id
import com.makentoshe.habrachan.entity.me.mobile.login
import com.makentoshe.habrachan.functional.Either2
import com.makentoshe.habrachan.network.me.mobile.MeUserManager
import com.makentoshe.habrachan.network.me.mobile.MeUserRequest
import com.makentoshe.habrachan.network.me.mobile.networkCode
import com.makentoshe.habrachan.network.me.mobile.networkMessage
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

class MeUserManagerTest {

    @Test
    fun `test should check response on success networking`() = runBlocking {
        val json = javaClass.classLoader.getResourceAsStream("me_user_success.json").readBytes().decodeToString()
        val request = MeUserRequest(AdditionalRequestParameters())

        val response = requireManagerWithJson(json, HttpStatusCode.OK).execute(request) as Either2.Left

        assertEquals(request, response.value.request)
        assertEquals("Makentoshe", response.value.me.login.value.string)
        assertEquals(1961555, response.value.me.id.value.int)
    }

    @Test
    fun `test should check response on success networking with failure response`() = runBlocking {
        val request = MeUserRequest(AdditionalRequestParameters())

        val response = requireManagerWithJson("null", HttpStatusCode.OK).execute(request) as Either2.Right

        assertEquals(request, response.value.request)
        assertEquals(HttpStatusCode.OK.value, response.value.networkCode.getOrThrow())
        assertEquals("null", response.value.networkMessage.getOrThrow())
    }

    @Test
    fun `test should check response on failure network`() = runBlocking {
        val request = MeUserRequest(AdditionalRequestParameters())
        val response = requireManagerWithException(UnresolvedAddressException()).execute(request) as Either2.Right

        assertEquals(request, response.value.request)
        assert(response.value.cause is UnresolvedAddressException)
        assertNull(response.value.networkCode.getOrNull())
        assertNull(response.value.networkMessage.getOrNull())
    }



    private fun requireManagerWithJson(json: String, status: HttpStatusCode): MeUserManager {
        return requireManagerWithNetwork {
            respond(ByteReadChannel(json), status, headersOf(HttpHeaders.ContentType, "application/json"))
        }
    }

    private fun requireManagerWithNetwork(
        action: MockRequestHandleScope.(HttpRequestData) -> HttpResponseData
    ): MeUserManager {
        return MeUserManager(HttpClient(MockEngine { request -> action(request) }))
    }

    private fun requireManagerWithException(exception: Exception): MeUserManager {
        return MeUserManager(HttpClient(MockEngine { request -> throw exception }))
    }
}