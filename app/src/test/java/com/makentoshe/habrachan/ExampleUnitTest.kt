package com.makentoshe.habrachan

import com.makentoshe.habrachan.common.model.network.HabrManager
import com.makentoshe.habrachan.common.model.network.posts.GetPostsRequest
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
        val manager = HabrManager.build()
        val request = GetPostsRequest(date = "day", page = 1)
        val result = manager.getPosts(request)
        println(result)
    }
}
