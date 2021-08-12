import com.makentoshe.habrachan.functional.getOrThrow
import com.makentoshe.habrachan.network.NativeGetArticlesDeserializer
import com.makentoshe.habrachan.network.NativeGetArticlesException
import com.makentoshe.habrachan.network.NativeGetArticlesManager
import com.makentoshe.habrachan.network.api.NativeArticlesApi
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import javax.net.ssl.SSLHandshakeException

class NativeGetArticlesManagerTest : RetrofitUnitTest() {

    private val mockNativeArticlesApi = mockk<NativeArticlesApi>()
    private val manager = NativeGetArticlesManager(mockNativeArticlesApi, NativeGetArticlesDeserializer())

    @Test
    fun testManagerShouldReturnArticlesOnSuccess() = runBlocking {
        every {
            mockNativeArticlesApi.getArticles(any(), any(), any(), any(), any(), any(), any(), any())
        } returns mockedCallResponse {
            mockedResponse(isSuccessful = true, code = 200) {
                mockedResponseBody(getResourceString("get_articles_success.json"))
            }
        }

        val request = manager.request(userSession, 0, manager.specs.first())
        val response = manager.articles(request).getOrThrow()

        assertEquals(request, response.request)
        assertEquals(20, response.articles.size)
        assertEquals(2, response.pagination.next?.number)
    }

    @Test
    fun testManagerShouldReturnExceptionOnNetworkError() = runBlocking {
        val exception = SSLHandshakeException("Exception created for test purposes")
        every {
            mockNativeArticlesApi.getArticles(any(), any(), any(), any(), any(), any(), any(), any())
        } throws exception

        val request = manager.request(userSession, 0, manager.specs.first())
        val response = manager.articles(request).exceptionOrNull() as NativeGetArticlesException

        assertEquals(request, response.request)
        assertEquals(exception, response.cause)
    }

    @Test
    fun testManagerShouldReturnExceptionOnDeserializeError() = runBlocking {
        val json = "{ just a malformed json }"
        every {
            mockNativeArticlesApi.getArticles(any(), any(), any(), any(), any(), any(), any(), any())
        } returns mockedCallResponse {
            mockedResponse(isSuccessful = true, code = 200) {
                mockedResponseBody(json)
            }
        }

        val request = manager.request(userSession, 0, manager.specs.first())
        val response = manager.articles(request).exceptionOrNull() as NativeGetArticlesException

        assertEquals(request, response.request)
        assertEquals(json, response.raw)
    }

    @Test
    fun testManagerShouldReturnExceptionOnNetworkErrorParse() = runBlocking {
        val json = getResourceString("get_articles_failure.json")
        every {
            mockNativeArticlesApi.getArticles(any(), any(), any(), any(), any(), any(), any(), any())
        } returns mockedCallResponse {
            mockedResponse(isSuccessful = true, code = 200) {
                mockedResponseBody(json)
            }
        }

        val request = manager.request(userSession, 0, manager.specs.first())
        val response = manager.articles(request).exceptionOrNull() as NativeGetArticlesException

        assertEquals(request, response.request)
        assertEquals(json, response.raw)
    }

//    @Test
//    fun testShouldCheckAllArticles() = runBlocking {
//        val request = manager.request(userSession, 1, manager.spec(SpecType.All)!!)
//        val response = manager.articles(request)
//
//        val articleResponse = response.getOrThrow()
//        assertEquals(request, articleResponse.request)
//        assertEquals(2, articleResponse.pagination.next?.number)
//
//        println(response)
//    }
//
//    @Test
//    fun testShouldCheckInterestingArticles() = runBlocking {
//        val request = manager.request(userSession, 1, manager.spec(SpecType.Interesting)!!)
//        val response = manager.articles(request)
//
//        val articleResponse = response.getOrThrow()
//        assertEquals(request, articleResponse.request)
//        assertEquals(2, articleResponse.pagination.next?.number)
//    }
//
//    @Test
//    fun testShouldCheckDailyTopArticles() = runBlocking {
//        val request = manager.request(userSession, 1, manager.spec(SpecType.Top(TopSpecType.Daily))!!)
//        val response = manager.articles(request)
//
//        val articleResponse = response.getOrThrow()
//        assertEquals(request, articleResponse.request)
//    }
}