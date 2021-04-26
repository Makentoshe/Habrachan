package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.network.deserializer.NativeGetCommentsDeserializer
import com.makentoshe.habrachan.network.userSession
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Ignore
import org.junit.Test

class NativeGetCommentsManagerTest {

    @Test
    @Ignore
    fun networkSuccess() = runBlocking {
        val userSession = userSession("85cab69095196f3.89453480", "173984950848a2d27c0cc1c76ccf3d6d3dc8255b")
        val manager = NativeGetCommentsManager.Builder(OkHttpClient(), NativeGetCommentsDeserializer()).build()
        val request = manager.request(userSession, 442440)
        val response = manager.comments(request)
        println(response)
    }
}