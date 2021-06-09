package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.entity.articleId
import com.makentoshe.habrachan.network.deserializer.NativeGetArticleDeserializer
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Assert.assertEquals
import org.junit.Test

class NativeGetArticleManagerTest : NativeUnitTest() {

    private val manager = NativeGetArticleManager.Builder(OkHttpClient(), NativeGetArticleDeserializer()).build()

    @Test
    fun testShould() = runBlocking {
        val request = manager.request(userSession, articleId(442440))
        val response = manager.article(request)

        val articleResponse = response.getOrThrow()
        assertEquals(request, articleResponse.request)
        assertEquals(442440, articleResponse.article.id)
        assertEquals("Makentoshe", articleResponse.article.author.login)

        println(response)
    }
}