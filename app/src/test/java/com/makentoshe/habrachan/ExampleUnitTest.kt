package com.makentoshe.habrachan

import com.makentoshe.habrachan.common.network.manager.LoginManager
import com.makentoshe.habrachan.common.network.request.LoginRequest
import okhttp3.OkHttpClient
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun sas() {
        val client = OkHttpClient()
        val manager = LoginManager.Builder(client).build()
        val request = LoginRequest.Builder(
            "85cab69095196f3.89453480", "173984950848a2d27c0cc1c76ccf3d6d3dc8255b"
        ).build("testmail@gmail.com", "password")
        val response = manager.login(request).blockingGet()
        println(response)
    }
}
