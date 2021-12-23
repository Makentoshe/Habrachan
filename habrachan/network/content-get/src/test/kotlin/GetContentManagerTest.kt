import com.makentoshe.habrachan.api.AdditionalRequestParameters
import com.makentoshe.habrachan.functional.Either2
import com.makentoshe.habrachan.network.content.get.*
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

class GetContentManagerTest {

    @Test
    fun `test should check response on success networking`() = runBlocking {
        val request = GetContentRequest("url", AdditionalRequestParameters())

        val response = requireManagerWithBytes("sas".toByteArray(), HttpStatusCode.OK).execute(request) as Either2.Left

        assertEquals(request, response.value.request)
        assertEquals("sas", response.value.content.bytes.decodeToString())
    }

    @Test
    fun `test should check response on failure network`() = runBlocking {
        val request = GetContentRequest("url", AdditionalRequestParameters())
        val response = requireManagerWithException(UnresolvedAddressException()).execute(request) as Either2.Right

        assertEquals(request, response.value.request)
        assert(response.value.cause is UnresolvedAddressException)
        assertNull(response.value.networkCode.getOrNull())
        assertNull(response.value.networkMessage.getOrNull())
    }

    private fun requireManagerWithBytes(byteArray: ByteArray, status: HttpStatusCode): GetContentManager {
        return requireManagerWithNetwork { respond(ByteReadChannel(byteArray), status) }
    }

    private fun requireManagerWithNetwork(
        action: MockRequestHandleScope.(HttpRequestData) -> HttpResponseData
    ): GetContentManager {
        return DefaultGetContentManager(HttpClient(MockEngine { request -> action(request) }))
    }

    private fun requireManagerWithException(exception: Exception): DefaultGetContentManager {
        return DefaultGetContentManager(HttpClient(MockEngine { request -> throw exception }))
    }
}