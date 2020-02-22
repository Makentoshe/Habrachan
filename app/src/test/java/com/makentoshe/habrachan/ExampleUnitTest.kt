package com.makentoshe.habrachan

import com.makentoshe.habrachan.common.entity.session.UserSession
import com.makentoshe.habrachan.common.network.manager.HabrArticleManager
import com.makentoshe.habrachan.common.network.manager.LoginManager
import com.makentoshe.habrachan.common.network.request.LoginRequest
import com.makentoshe.habrachan.common.network.request.VoteUpArticleRequest
import okhttp3.OkHttpClient
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    private val session = UserSession(
        "85cab69095196f3.89453480",
        "173984950848a2d27c0cc1c76ccf3d6d3dc8255b",
        File("token").readText()
    )

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

    @Test
    fun voteUpArticleTest() {
        val articleId = 489532
        val manager = HabrArticleManager.Builder(OkHttpClient()).build()
        val request = VoteUpArticleRequest(session.clientKey, session.tokenKey!!, articleId)
        val response = manager.voteUp(request).blockingGet()
        println(response)
    }
}
