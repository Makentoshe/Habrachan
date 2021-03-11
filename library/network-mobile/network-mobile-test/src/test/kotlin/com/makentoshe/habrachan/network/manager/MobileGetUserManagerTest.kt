package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.network.deserializer.MobileGetUserDeserializer
import com.makentoshe.habrachan.network.userSession
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Ignore
import org.junit.Test

class MobileGetUserManagerTest {

    @Test
    @Ignore
    fun networkSuccess() = runBlocking {
        val userSession = userSession("", "")
        val manager = MobileGetUserManager.Builder(OkHttpClient(), MobileGetUserDeserializer()).build()
        val request = manager.request(userSession, "Makentoshe")
        val response = manager.user(request)
        println(response)
    }
}