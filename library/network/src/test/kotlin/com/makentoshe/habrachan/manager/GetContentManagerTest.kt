package com.makentoshe.habrachan.manager

import com.makentoshe.habrachan.network.manager.GetContentManager
import com.makentoshe.habrachan.network.userSession
import okhttp3.OkHttpClient
import org.junit.Test

class GetContentManagerTest {

    @Test
    fun networkSuccess() {
        val url = "https://habr.com"

        val manager = GetContentManager(OkHttpClient())
        val request = manager.request(userSession("", ""), url)
        val response = manager.content(request)
        println(response)
    }
}