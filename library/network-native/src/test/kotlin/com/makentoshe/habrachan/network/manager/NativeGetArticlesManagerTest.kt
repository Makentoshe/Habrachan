package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.network.deserializer.NativeGetArticlesDeserializer
import com.makentoshe.habrachan.network.request.SpecType
import com.makentoshe.habrachan.network.userSession
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Ignore
import org.junit.Test

class NativeGetArticlesManagerTest {

    @Test
    @Ignore
    fun network() = runBlocking {
        val userSession = userSession("", "")
        val manager = NativeGetArticlesManager.Builder(OkHttpClient(), NativeGetArticlesDeserializer()).build(userSession)
        val request = manager.request(2, manager.specs.find { it.type == SpecType.All }!!)
        val response = manager.articles(request)
        println(response)
    }

}