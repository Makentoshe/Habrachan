package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.network.deserializer.MobileGetUserDeserializer
import com.makentoshe.habrachan.network.deserializer.MobileLoginDeserializer
import com.makentoshe.habrachan.network.userSession
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Ignore
import org.junit.Test

class MobileLoginManagerTest {

    private val client = OkHttpClient()

    // TODO test with empty login or password
    @Test
    @Ignore
    fun networkSuccess() = runBlocking {
        val userSession = userSession("", "")

        val userManager = MobileGetUserManager.Builder(client, MobileGetUserDeserializer()).build()
        val loginManager = MobileLoginManager.Builder(OkHttpClient(), MobileLoginDeserializer(), userManager).build()

        val request = loginManager.request(userSession, "", "")
        val response = loginManager.login(request)

        println(response)
    }
}