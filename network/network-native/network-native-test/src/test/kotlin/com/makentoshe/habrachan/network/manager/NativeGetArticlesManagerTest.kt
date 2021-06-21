package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.network.deserializer.NativeGetArticlesDeserializer
import com.makentoshe.habrachan.network.request.SpecType
import com.makentoshe.habrachan.network.request.TopSpecType
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Assert.assertEquals
import org.junit.Test

class NativeGetArticlesManagerTest : NativeUnitTest() {

    private val manager = NativeGetArticlesManager.Builder(OkHttpClient(), NativeGetArticlesDeserializer()).build()

    @Test
    fun testShouldCheckAllArticles() = runBlocking {
        val request = manager.request(userSession, 1, manager.spec(SpecType.All)!!)
        val response = manager.articles(request)

        val articleResponse = response.getOrThrow()
        assertEquals(request, articleResponse.request)
        assertEquals(2, articleResponse.pagination.next?.number)

        println(response)
    }

    @Test
    fun testShouldCheckInterestingArticles() = runBlocking {
        val request = manager.request(userSession, 1, manager.spec(SpecType.Interesting)!!)
        val response = manager.articles(request)

        val articleResponse = response.getOrThrow()
        assertEquals(request, articleResponse.request)
        assertEquals(2, articleResponse.pagination.next?.number)
    }

    @Test
    fun testShouldCheckDailyTopArticles() = runBlocking {
        val request = manager.request(userSession, 1, manager.spec(SpecType.Top(TopSpecType.Daily))!!)
        val response = manager.articles(request)

        val articleResponse = response.getOrThrow()
        assertEquals(request, articleResponse.request)
    }
}