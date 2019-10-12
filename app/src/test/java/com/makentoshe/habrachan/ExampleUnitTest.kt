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
        var words = emptyArray<String>()
        var counts = emptyArray<Int>()
        val string = readLine()!!
        val split = string.split(" ")
        for (i in 0 until split.size) {
            val word = split[i]
            if (words.contains(word)) {
                val index = words.indexOf(word)
                counts[index] = counts[index].inc()
            } else {
                words = words.plusElement(word)
                counts = counts.plusElement(1)
            }
        }
    }
}
