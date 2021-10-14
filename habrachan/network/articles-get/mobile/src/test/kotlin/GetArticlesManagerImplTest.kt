@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

import com.makentoshe.habrachan.api.AdditionalRequestParameters
import com.makentoshe.habrachan.api.articles.and
import com.makentoshe.habrachan.api.mobile.articles.RatingArticlesSort
import com.makentoshe.habrachan.api.mobile.articles.pageArticlesFilter
import com.makentoshe.habrachan.api.mobile.articles.sortArticlesFilter
import com.makentoshe.habrachan.functional.Either
import com.makentoshe.habrachan.network.articles.get.GetArticlesManagerImpl
import com.makentoshe.habrachan.network.articles.get.GetArticlesRequest
import com.makentoshe.habrachan.network.articles.get.entity.articles
import com.makentoshe.habrachan.network.articles.get.entity.pages
import com.makentoshe.habrachan.network.articles.get.networkCode
import com.makentoshe.habrachan.network.articles.get.networkMessage
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import java.nio.channels.UnresolvedAddressException

class GetArticlesManagerImplTest {

    @Test
    fun `test should check response on success networking`() = runBlocking {
        val json = javaClass.classLoader.getResourceAsStream("articles_success.json").readAllBytes().decodeToString()

        val filters = sortArticlesFilter(RatingArticlesSort) and pageArticlesFilter(2)
        val request = GetArticlesRequest(AdditionalRequestParameters(), filters)

        val response = requireManagerWithJson(json, HttpStatusCode.OK).execute(request) as Either.Left

        assertEquals(request, response.value.request)
        assertEquals(50, response.value.articles.pages.value)
        assertEquals(20, response.value.articles.articles.value.size)
    }

    @Test
    fun `test should check response on success networking with failure response`() = runBlocking {
        val json = javaClass.classLoader.getResourceAsStream("articles_failure.json").readAllBytes().decodeToString()

        val filters = pageArticlesFilter(2)
        val request = GetArticlesRequest(AdditionalRequestParameters(), arrayOf(filters))

        val response = requireManagerWithJson(json, HttpStatusCode.BadRequest).execute(request) as Either.Right

        assertEquals(request, response.value.request)
        assertEquals(HttpStatusCode.BadRequest.value, response.value.networkCode.getOrThrow())
        assertEquals("Bad Request Url", response.value.networkMessage.getOrThrow())
    }

    @Test
    fun `test should check response on failure network`() = runBlocking {
        val filters = sortArticlesFilter(RatingArticlesSort) and pageArticlesFilter(2)
        val request = GetArticlesRequest(AdditionalRequestParameters(), filters)

        val response = requireManagerWithException(UnresolvedAddressException()).execute(request) as Either.Right

        assertEquals(request, response.value.request)
        assert(response.value.cause is UnresolvedAddressException)
        assert(response.value.networkCode.isEmpty)
        assert(response.value.networkMessage.isEmpty)
    }

    private fun requireManagerWithJson(json: String, status: HttpStatusCode): GetArticlesManagerImpl {
        return requireManagerWithNetwork {
            respond(ByteReadChannel(json), status, headersOf(HttpHeaders.ContentType, "application/json"))
        }
    }

    private fun requireManagerWithNetwork(
        action: MockRequestHandleScope.(HttpRequestData) -> HttpResponseData
    ): GetArticlesManagerImpl {
        return GetArticlesManagerImpl(HttpClient(MockEngine { request -> action(request) }))
    }

    private fun requireManagerWithException(exception: Exception): GetArticlesManagerImpl {
        return GetArticlesManagerImpl(HttpClient(MockEngine { request -> throw exception}))
    }
}