@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

import com.makentoshe.habrachan.api.AdditionalRequestParameters
import com.makentoshe.habrachan.entity.article.articleTitle
import com.makentoshe.habrachan.entity.article.component.ArticleId
import com.makentoshe.habrachan.functional.Either2
import com.makentoshe.habrachan.network.article.get.GetArticleRequest
import com.makentoshe.habrachan.network.article.get.mobile.GetArticleManagerImpl
import com.makentoshe.habrachan.network.article.get.mobile.networkCode
import com.makentoshe.habrachan.network.article.get.mobile.networkMessage
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.network.*
import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class GetArticleManagerImplTest {

    @Test
    fun `test should check response on success networking`() = runBlocking {
        val json = javaClass.classLoader.getResourceAsStream("article_success.json").readBytes().decodeToString()

        val request = GetArticleRequest(ArticleId(123), AdditionalRequestParameters())

        val response = requireManagerWithJson(json, HttpStatusCode.OK).execute(request) as Either2.Left

        assertEquals(request, response.value.request)
        assertEquals(
            "(не) Безопасный дайджест New Year Edition: атака на принтеры, корпоративный шпионаж и похищенные YouTube-каналы",
            response.value.article.articleTitle.value.articleTitle,
        )
    }

    @Test
    fun `test should check response on success networking with failure response`() = runBlocking {
        val json = javaClass.classLoader.getResourceAsStream("article_failure.json").readBytes().decodeToString()

        val request = GetArticleRequest(ArticleId(123), AdditionalRequestParameters())
        val response = requireManagerWithJson(json, HttpStatusCode.BadRequest).execute(request) as Either2.Right

        assertEquals(request, response.value.request)
        assertEquals(HttpStatusCode.NotFound.value, response.value.networkCode.getOrThrow())
        assertEquals("Resource not found", response.value.networkMessage.getOrThrow())
    }

    @Test
    fun `test should check response on failure network`() = runBlocking {
        val request = GetArticleRequest(ArticleId(123), AdditionalRequestParameters())
        val response = requireManagerWithException(UnresolvedAddressException()).execute(request) as Either2.Right

        assertEquals(request, response.value.request)
        assert(response.value.cause is UnresolvedAddressException)
        assert(response.value.networkCode.isEmpty)
        assert(response.value.networkMessage.isEmpty)
    }

    private fun requireManagerWithJson(json: String, status: HttpStatusCode): GetArticleManagerImpl {
        return requireManagerWithNetwork {
            respond(ByteReadChannel(json), status, headersOf(HttpHeaders.ContentType, "application/json"))
        }
    }

    private fun requireManagerWithNetwork(
        action: MockRequestHandleScope.(HttpRequestData) -> HttpResponseData,
    ): GetArticleManagerImpl {
        return GetArticleManagerImpl(HttpClient(MockEngine { request -> action(request) }))
    }

    private fun requireManagerWithException(exception: Exception): GetArticleManagerImpl {
        return GetArticleManagerImpl(HttpClient(MockEngine { request -> throw exception }))
    }
}