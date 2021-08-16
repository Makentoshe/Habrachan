package com.makentoshe.habrachan.network

import RetrofitUnitTest
import com.makentoshe.habrachan.entity.articleId
import com.makentoshe.habrachan.functional.getOrThrow
import com.makentoshe.habrachan.network.natives.api.NativeArticlesApi
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import javax.net.ssl.SSLHandshakeException

class NativeGetArticleManagerTest : RetrofitUnitTest() {

    private val mockNativeArticlesApi = mockk<NativeArticlesApi>()
    private val manager = NativeGetArticleManager(mockNativeArticlesApi, NativeGetArticleDeserializer())

    @Test
    fun testManagerShouldReturnArticleOnSuccess() = runBlocking {
        every {
            mockNativeArticlesApi.getArticle(any(), any(), any(), any(), any(), any(), any())
        } returns mockedCallResponse {
            mockedResponse {
                mockedResponseBody(getResourceString("get_article_success.json"))
            }
        }

        val request = manager.request(userSession, articleId(0))
        val response = manager.article(request).getOrThrow()

        assertEquals(response.request, request)
        assertEquals(442440, response.article.id)
    }

    @Test
    fun testManagerShouldReturnExceptionOnNetworkFailure() = runBlocking {
        val exception = SSLHandshakeException("Custom network exception for test purposes")
        every {
            mockNativeArticlesApi.getArticle(any(), any(), any(), any(), any(), any(), any())
        } throws exception

        val request = manager.request(userSession, articleId(0))
        val response = manager.article(request).exceptionOrNull() as NativeGetArticleException

        assertEquals(request, response.request)
        assertEquals(response.cause, exception)
    }

    @Test
    fun testManagerShouldReturnExceptionOnDeserializerFailure() = runBlocking {
        every {
            mockNativeArticlesApi.getArticle(any(), any(), any(), any(), any(), any(), any())
        } returns mockedCallResponse {
            mockedResponse(isSuccessful = true, code = 200) {
                mockedResponseBody("Sas")
            }
        }

        val request = manager.request(userSession, articleId(0))
        val response = manager.article(request).exceptionOrNull() as NativeGetArticleException

        assertEquals(request, response.request)
        assertEquals(response.message, "Sas")
    }

    @Test
    fun testManagerShouldReturnExceptionOnClientProblem() = runBlocking {
        val string = getResourceString("get_article_failure.json")
        every {
            mockNativeArticlesApi.getArticle(any(), any(), any(), any(), any(), any(), any())
        } returns mockedCallResponse {
            mockedResponse(isSuccessful = false, code = 401) {
                mockedResponseBody(string)
            }
        }

        val request = manager.request(userSession, articleId(0))
        val response = manager.article(request).exceptionOrNull() as NativeGetArticleException

        assertEquals(request, response.request)
        assertEquals(response.message, string)
    }

}