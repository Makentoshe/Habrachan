@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

import com.makentoshe.habrachan.api.AdditionalRequestParameters
import com.makentoshe.habrachan.api.android.articles.allArticlesFilter
import com.makentoshe.habrachan.api.android.articles.pageArticlesFilter
import com.makentoshe.habrachan.api.android.articles.topArticlesFilter
import com.makentoshe.habrachan.api.articles.and
import com.makentoshe.habrachan.api.articles.filter.WeeklyArticlesPeriod
import com.makentoshe.habrachan.functional.Either
import com.makentoshe.habrachan.network.articles.get.GetArticlesRequest
import com.makentoshe.habrachan.network.articles.get.android.GetArticlesManagerImpl
import com.makentoshe.habrachan.network.articles.get.android.entity.*
import com.makentoshe.habrachan.network.articles.get.android.networkAdditional
import com.makentoshe.habrachan.network.articles.get.android.networkCode
import com.makentoshe.habrachan.network.articles.get.android.networkMessage
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import java.nio.channels.UnresolvedAddressException

class GetArticlesNetworkManagerImplTest {

    private val parameters = AdditionalRequestParameters(
        headers = mapOf(
            "client" to "85cab69095196f3.89453480",
            "apiKey" to "173984950848a2d27c0cc1c76ccf3d6d3dc8255b"
        )
    )

    @Test
    fun `test should check response on success networking`() = runBlocking {
        val json = javaClass.classLoader.getResourceAsStream("articles_success.json").readAllBytes().decodeToString()

        val filters = allArticlesFilter() and pageArticlesFilter(2)
        val request = GetArticlesRequest(parameters, filters)

        val response = requireManagerWithJson(json, HttpStatusCode.OK).execute(request) as Either.Left

        assertEquals(request, response.value.request)
        assertEquals(50, response.value.articles.pages.value)
        assertEquals("2021-10-10T20:55:28+03:00", response.value.articles.serverTime.value)
        assertEquals("time_published", response.value.articles.sortedBy.value)
        assertEquals("https://habr.com/api/v1/posts/all?page=3", response.value.articles.nextPage.getOrThrow().url.value)
        assertEquals(3, response.value.articles.nextPage.getOrThrow().int.value)
    }

    @Test
    fun `test should check response on success networking with failure response`() = runBlocking {
        val json = javaClass.classLoader.getResourceAsStream("articles_failure.json").readAllBytes().decodeToString()

        val filters = pageArticlesFilter(2) and topArticlesFilter(WeeklyArticlesPeriod)
        val request = GetArticlesRequest(AdditionalRequestParameters(), filters)

        val response = requireManagerWithJson(json, HttpStatusCode.Unauthorized).execute(request) as Either.Right

        assertEquals(request, response.value.request)
        assertEquals(HttpStatusCode.Unauthorized.value, response.value.networkCode.getOrThrow())
        assertEquals("Authorization required", response.value.networkMessage.getOrThrow())
        assertEquals("TOKEN_PROBLEM", response.value.networkAdditional.getOrThrow().first())
    }

    @Test
    fun `test should check response on failure network`() = runBlocking {
        val filters = pageArticlesFilter(2) and topArticlesFilter(WeeklyArticlesPeriod)
        val request = GetArticlesRequest(AdditionalRequestParameters(), filters)

        val response = requireManagerWithException(UnresolvedAddressException()).execute(request) as Either.Right

        assertEquals(request, response.value.request)
        assert(response.value.cause is UnresolvedAddressException)
        assert(response.value.networkCode.isEmpty)
        assert(response.value.networkMessage.isEmpty)
        assert(response.value.networkAdditional.isEmpty)
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
        return GetArticlesManagerImpl(HttpClient(MockEngine { request -> throw exception }))
    }
}
