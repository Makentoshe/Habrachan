package com.makentoshe.habrachan

import com.makentoshe.habrachan.common.network.request.GetPostsRequest
import com.makentoshe.habrachan.common.network.manager.HabrPostsManager
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
    fun asa() {
        val manager = HabrPostsManager.build()
        val request = GetPostsRequest(
            client = "85cab69095196f3.89453480",
            token = "ee828f6b64a066b352dc18e3034038c905c4d8ca",
            api = null,
            page = 1,
            path1 = "posts",
            path2 = "interesting"
        )
        val result = manager.getPosts(request).blockingGet()
        println(result)
    }
}
