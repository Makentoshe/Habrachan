package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.network.deserializer.NativeGetUserDeserializer
import com.makentoshe.habrachan.network.deserializer.NativeLoginDeserializer
import com.makentoshe.habrachan.network.userSession
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Ignore
import org.junit.Test

class NativeLoginManagerTest {

    @Test
    @Ignore
    fun networkSuccess() = runBlocking {
        val userSession = userSession("85cab69095196f3.89453480", "173984950848a2d27c0cc1c76ccf3d6d3dc8255b")
        val manager = NativeLoginManager.Builder(OkHttpClient(), NativeLoginDeserializer(), NativeGetUserDeserializer()).build()
        val request = manager.request(userSession, "", "")
        val response = manager.login(request)
        println(response)
    }
}