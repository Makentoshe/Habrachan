package com.makentoshe.habrachan

import com.makentoshe.habrachan.common.model.network.postsalt.*
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
        val manager = HabrPostsManager.build()
        val request = GetFeedRequest(
            client = "85cab69095196f3.89453480",
//            api = "173984950848a2d27c0cc1c76ccf3d6d3dc8255b",
            token = "ee828f6b64a066b352dc18e3034038c905c4d8ca",
            page = 1
        )
        manager.getFeed(request).subscribe { s, t ->
           println(s)
        }
        Thread.sleep(100000)
    }
}
