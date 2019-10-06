package com.makentoshe.habrachan

import com.makentoshe.habrachan.common.model.network.HabrManager
import com.makentoshe.habrachan.common.model.network.posts.bydate.GetPostsByDate
import com.makentoshe.habrachan.common.model.network.posts.bydate.GetPostsByDateRequest
import com.makentoshe.habrachan.common.model.network.posts.bysort.GetPostsBySortRequest
import org.junit.Test

import org.junit.Assert.*

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
        val request = GetPostsByDateRequest(date = "day", page = 1)
        val result = manager.getPosts(request)
        println(result)
    }
}
