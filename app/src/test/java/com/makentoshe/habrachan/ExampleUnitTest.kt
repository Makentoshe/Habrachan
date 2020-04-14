package com.makentoshe.habrachan

import com.makentoshe.habrachan.common.entity.session.UserSession
import com.makentoshe.habrachan.common.network.manager.HabrArticleManager
import com.makentoshe.habrachan.common.network.manager.ImageManager
import com.makentoshe.habrachan.common.network.request.GetArticleRequest
import com.makentoshe.habrachan.common.network.request.GetArticlesRequest
import com.makentoshe.habrachan.common.network.request.ImageRequest
import com.makentoshe.habrachan.common.network.request.VoteArticleRequest
import okhttp3.OkHttpClient
import org.junit.Ignore
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest : BaseTest() {

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
        val manager = HabrArticleManager.Builder(OkHttpClient()).build()
        val request = GetArticlesRequest(session.clientKey, session.apiKey, session.tokenKey, 1, UserSession.ArticlesRequestSpec.all().request)
        val response = manager.getArticles(request).blockingGet()
        println(response)
    }

    @Test
    @Ignore
    fun getUserAvatarTest() {
        val avatarUrl = "https://habrastorage.org/getpro/habr/avatars/6d5/142/fd3/6d5142fd38ef294711e6ecb9e764f8ed.png"
        val avatarRequest = ImageRequest(avatarUrl)
        val manager = ImageManager.Builder(OkHttpClient()).build()
        val response = manager.getImage(avatarRequest).blockingGet()
        println(response)
    }
}
