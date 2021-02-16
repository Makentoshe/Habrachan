package com.makentoshe.habrachan.mobiles.network.manager

import com.makentoshe.habrachan.entity.articleId
import com.makentoshe.habrachan.network.deserializer.MobileGetArticleDeserializer
import com.makentoshe.habrachan.network.manager.MobileGetArticleManager
import com.makentoshe.habrachan.network.userSession
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Ignore
import org.junit.Test

class MobileGetArticleManagerTest {

    @Test
    @Ignore
    fun network() = runBlocking {
        val userSession = userSession("", "")
        val deserializer = MobileGetArticleDeserializer()

        val manager = MobileGetArticleManager.Builder(OkHttpClient(), deserializer).build()
        val request = manager.request(userSession, articleId(442440))
        val response = manager.article(request)
        println(response)
    }
}