package com.makentoshe.habrachan.mobiles.network.manager

import com.makentoshe.habrachan.network.deserializer.MobileGetArticlesDeserializer
import com.makentoshe.habrachan.network.manager.MobileGetArticlesManager
import com.makentoshe.habrachan.network.request.SpecType
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Ignore
import org.junit.Test

class MobileGetArticlesManagerTest {

    @Test
    @Ignore
    fun network() = runBlocking {
        val manager = MobileGetArticlesManager.Builder(OkHttpClient(), MobileGetArticlesDeserializer()).build()
        val request = manager.request(2, manager.specs.find { it.type == SpecType.All }!!)
        val response = manager.articles(request)
        println(response)
    }

}

