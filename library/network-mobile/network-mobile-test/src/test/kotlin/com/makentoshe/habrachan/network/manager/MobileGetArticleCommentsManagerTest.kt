package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.network.deserializer.MobileGetArticleCommentsDeserializer
import com.makentoshe.habrachan.network.userSession
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Test

class MobileGetArticleCommentsManagerTest {

    @Test
    fun networkSuccess() = runBlocking {
        val userSession = userSession("", "")
        val deserializer = MobileGetArticleCommentsDeserializer()

        val manager = MobileGetArticleCommentsManager.Builder(OkHttpClient(), deserializer).build()
        val request = manager.request(userSession, 442440)
        val response = manager.comments(request)
        println(response)
    }
}