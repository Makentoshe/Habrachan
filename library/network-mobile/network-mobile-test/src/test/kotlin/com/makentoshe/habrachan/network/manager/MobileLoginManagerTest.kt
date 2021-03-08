package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.network.userSession
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Ignore
import org.junit.Test

class MobileLoginManagerTest {

    @Test
    @Ignore
    fun networkSuccess() = runBlocking {
        val userSession = userSession("", "")
        val manager = MobileLoginManager.Builder(OkHttpClient()).build()
        val request = manager.request(userSession, "", "")
        val response = manager.login(request)
        println(response)
    }
}