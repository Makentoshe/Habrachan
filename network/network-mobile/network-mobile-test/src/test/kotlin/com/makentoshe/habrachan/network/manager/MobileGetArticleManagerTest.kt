package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.entity.articleId
import com.makentoshe.habrachan.network.deserializer.MobileGetArticleDeserializer
import com.makentoshe.habrachan.network.userSession
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Ignore
import org.junit.Test

class MobileGetArticleManagerTest {

    @Test
    @Ignore
    fun networkSuccess() = runBlocking {
        val userSession = userSession("", "")
        val deserializer = MobileGetArticleDeserializer()

        val manager = MobileGetArticleManager.Builder(OkHttpClient(), deserializer).build()
        val request = manager.request(userSession, articleId(442441230))
        val response = manager.article(request)
        println(response)
    }
}