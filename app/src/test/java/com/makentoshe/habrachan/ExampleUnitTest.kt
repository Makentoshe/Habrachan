package com.makentoshe.habrachan

import com.makentoshe.habrachan.common.network.manager.HabrCommentsManager
import com.makentoshe.habrachan.common.network.request.GetCommentsRequest
import com.makentoshe.habrachan.common.repository.InputStreamRepository
import okhttp3.OkHttpClient
import org.junit.Assert.assertEquals
import org.junit.Test
import java.net.MalformedURLException
import java.net.URL

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
        val repository = InputStreamRepository(client)
        val stream = repository.get("https://habrastorage.org/getpro/habr/formulas/08d/9fa/efb/08d9faefbe272bdf8fbb80773542e343.svg")
        val bytes = stream!!.readBytes()
        val string = String(bytes)
        println(string)
    }
}
