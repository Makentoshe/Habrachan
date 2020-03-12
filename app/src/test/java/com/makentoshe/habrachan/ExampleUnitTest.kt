package com.makentoshe.habrachan

import com.makentoshe.habrachan.common.entity.session.UserSession
import com.makentoshe.habrachan.common.network.manager.AvatarManager
import com.makentoshe.habrachan.common.network.manager.HabrArticleManager
import com.makentoshe.habrachan.common.network.manager.UsersManager
import com.makentoshe.habrachan.common.network.request.*
import okhttp3.OkHttpClient
import org.junit.Ignore
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
    @Ignore
    fun voteUpArticleTest() {
        val articleId = 489596
        val manager = HabrArticleManager.Builder(OkHttpClient()).build()
        val request = VoteArticleRequest(session.clientKey, session.tokenKey, articleId)
        val response = manager.voteUp(request).blockingGet()
        println(response)
    }

    @Test
    @Ignore
    fun voteDownArticleTest() {
        val articleId = 489440
        val manager = HabrArticleManager.Builder(OkHttpClient()).build()
        val request = VoteArticleRequest(session.clientKey, session.tokenKey, articleId)
        val response = manager.voteDown(request).blockingGet()
        println(response)
    }

    @Test
    @Ignore
    fun getSingleArticleTest() {
        val articleId = 489144
        val manager = HabrArticleManager.Builder(OkHttpClient()).build()
        val request = GetArticleRequest(session.clientKey, session.tokenKey, session.apiKey, articleId)
        val response = manager.getPost(request).blockingGet()
        println(response)
    }

    @Test
    @Ignore
    fun getAllArticlesTest() {
        val requestFactory = GetArticlesRequest.Factory(session.clientKey, session.apiKey, session.tokenKey)
        val manager = HabrArticleManager.Builder(OkHttpClient()).build()
        val request = requestFactory.all(1)
        val response = manager.getArticles(request).blockingGet()
        println(response)
    }

    @Test
    @Ignore
    fun getCustomUserByName() {
        val request = UserRequest(session.clientKey, session.apiKey, session.tokenKey, "missingdays")
        val manager = UsersManager.Builder(OkHttpClient()).build()
        val response = manager.getUser(request).blockingGet()
        println(response)
    }

    @Test
    @Ignore
    fun getUserAvatarTest() {
        val avatarUrl = "https://habrastorage.org/getpro/habr/avatars/6d5/142/fd3/6d5142fd38ef294711e6ecb9e764f8ed.png"
        val avatarRequest = AvatarRequest(avatarUrl)
        val manager = AvatarManager.Builder(OkHttpClient()).build()
        val response = manager.getAvatar(avatarRequest).blockingGet()
        println(response)
    }
}
