package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.entity.articleId
import com.makentoshe.habrachan.network.deserializer.NativeGetArticleDeserializer
import com.makentoshe.habrachan.network.userSession
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Ignore
import org.junit.Test

class NativeGetArticleManagerTest {

    @Test
    @Ignore
    fun networkSuccess() = runBlocking {
        val userSession = userSession("", "73984950848a2d27c0cc1c76ccf3d6d3dc8255b")
        val manager = NativeGetArticleManager.Builder(OkHttpClient(), NativeGetArticleDeserializer()).build()
        val request = manager.request(userSession, articleId(442440))
        val response = manager.article(request)

        println(response)
    }
}