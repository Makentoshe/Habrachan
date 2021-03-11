package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.network.deserializer.NativeGetUserDeserializer
import com.makentoshe.habrachan.network.userSession
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Ignore
import org.junit.Test

class NativeGetUserManagerTest {

    @Test
    @Ignore
    fun networkSuccess() = runBlocking {
        val token = ""
        val userSession = userSession("85cab69095196f3.89453480", "73984950848a2d27c0cc1c76ccf3d6d3dc8255b", token)
        val manager = NativeGetUserManager.Builder(OkHttpClient(), NativeGetUserDeserializer()).build()
        val request = manager.request(userSession, "Makentoshe")
        val response = manager.user(request)
        println(response)
    }
}