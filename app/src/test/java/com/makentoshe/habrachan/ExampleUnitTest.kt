package com.makentoshe.habrachan

import com.makentoshe.habrachan.common.network.manager.HabrPostsManager
import com.makentoshe.habrachan.common.network.request.GetPostsRequestFactory
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
        val client = OkHttpClient.Builder().build()
        val manager = HabrPostsManager.Builder(client).build()
        val factory = GetPostsRequestFactory(
            client = "85cab69095196f3.89453480", api = "173984950848a2d27c0cc1c76ccf3d6d3dc8255b", token = null
        )
        val request = factory.query(1, "anko")
        val result = manager.getPosts(request).blockingGet()
        println(result)
    }
}
