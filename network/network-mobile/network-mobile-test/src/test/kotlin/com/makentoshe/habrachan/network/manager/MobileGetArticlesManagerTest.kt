package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.network.deserializer.MobileGetArticlesDeserializer
import com.makentoshe.habrachan.network.request.SpecType
import com.makentoshe.habrachan.network.userSession
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Ignore
import org.junit.Test

class MobileGetArticlesManagerTest {

    @Test
    @Ignore
    fun network() = runBlocking {
        val userSession = userSession("", "")
        val manager = MobileGetArticlesManager.Builder(OkHttpClient(), MobileGetArticlesDeserializer()).build()
        val request = manager.request(userSession, 2, manager.specs.find { it.type == SpecType.All }!!)
        val response = manager.articles(request)
        println(response)
    }

}

