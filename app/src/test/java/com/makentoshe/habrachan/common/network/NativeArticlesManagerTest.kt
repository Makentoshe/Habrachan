package com.makentoshe.habrachan.common.network

import com.makentoshe.habrachan.BaseTest
import com.makentoshe.habrachan.ResponseInterceptor
import com.makentoshe.habrachan.UrlInterceptor
import com.makentoshe.habrachan.common.entity.session.ArticlesRequestSpec
import com.makentoshe.habrachan.common.network.manager.ArticlesManager
import com.makentoshe.habrachan.common.network.request.GetArticleRequest
import com.makentoshe.habrachan.common.network.request.GetArticlesRequest
import com.makentoshe.habrachan.common.network.request.VoteArticleRequest
import com.makentoshe.habrachan.common.network.response.ArticleResponse
import com.makentoshe.habrachan.common.network.response.ArticlesResponse
import okhttp3.OkHttpClient
import org.junit.Assert
import org.junit.Ignore
import org.junit.Test

class NativeArticlesManagerTest : BaseTest() {

    @Test
    fun `should parse and return success result for get all articles action`() {
        val spec = ArticlesRequestSpec.All()
        val page = 1
        val json = getJsonResponse("get_articles_success_native.json")
        val url = "https://habr.com/api/v1/posts%2Fall?page=$page&include=${spec.include}"

        val request = GetArticlesRequest(session, 1, spec)

        val client = OkHttpClient.Builder()
            .addInterceptor(ResponseInterceptor(200, json))
            .addInterceptor(UrlInterceptor(url))
            .build()
        val manager = ArticlesManager.Builder(client).build()
        val response = manager.getArticles(request).blockingGet() as ArticlesResponse.Success

        Assert.assertEquals(20, response.data.size)
    }

    @Test
    fun `should parse and return error result for get all articles action`() {
        val spec = ArticlesRequestSpec.All()
        val page = 1
        val json = getJsonResponse("get_articles_error_native.json")
        val url = "https://habr.com/api/v1/posts%2Fall?page=$page&include=${spec.include}"

        val request = GetArticlesRequest(session, 1, spec)

        val client = OkHttpClient.Builder()
            .addInterceptor(ResponseInterceptor(401, json))
            .addInterceptor(UrlInterceptor(url))
            .build()
        val manager = ArticlesManager.Builder(client).build()
        val response = manager.getArticles(request).blockingGet() as ArticlesResponse.Error

        Assert.assertEquals(json, response.json)
    }

    @Test
    fun `should parse and return success result for get article action`() {
        val postId = 123
        val json = getJsonResponse("get_article_success.json")
        val url = "https://habr.com/api/v1/post/$postId?include=text_html"

        val request = GetArticleRequest(session, postId)

        val client = OkHttpClient.Builder()
            .addInterceptor(ResponseInterceptor(200, json))
            .addInterceptor(UrlInterceptor(url))
            .build()
        val manager = ArticlesManager.Builder(client).build()
        val response = manager.getArticle(request).blockingGet() as ArticleResponse.Success

        Assert.assertEquals("2020-04-21T21:29:45+03:00", response.serverTime)
        Assert.assertEquals(496922, response.article.id)
    }

    @Test
    fun `should parse and return error result for get article action`() {
        val postId = 123
        val json = getJsonResponse("get_article_error.json")
        val url = "https://habr.com/api/v1/post/$postId?include=text_html"

        val request = GetArticleRequest(session, postId)

        val client = OkHttpClient.Builder()
            .addInterceptor(ResponseInterceptor(400, json))
            .addInterceptor(UrlInterceptor(url))
            .build()
        val manager = ArticlesManager.Builder(client).build()
        val response = manager.getArticle(request).blockingGet() as ArticleResponse.Error

        Assert.assertEquals(json, response.json)
    }

    @Test
    @Ignore("Test uses real api")
    fun getAllArticlesTest() {
        val manager = ArticlesManager.Builder(OkHttpClient()).build()
        val spec = ArticlesRequestSpec.All()
        val request = GetArticlesRequest(session, 1, spec)
        val response = manager.getArticles(request).blockingGet()
        println(response)
    }

    @Test
    @Ignore("Test uses real api")
    fun getTopArticlesTest() {
        val manager = ArticlesManager.Builder(OkHttpClient()).build()
        val spec = ArticlesRequestSpec.Top(ArticlesRequestSpec.Top.Type.AllTime)
        val request = GetArticlesRequest(session, 1, spec)
        val response = manager.getArticles(request).blockingGet()
        println(response)
    }

    @Test
    @Ignore("Test uses real api")
    fun getSingleArticleTest() {
        val articleId = 489144
        val manager = ArticlesManager.Builder(OkHttpClient()).build()
        val request = GetArticleRequest(session.clientKey, session.tokenKey, session.apiKey, articleId)
        val response = manager.getArticle(request).blockingGet()
        println(response)
    }


    @Test
    @Ignore("Test uses real api")
    fun voteUpArticleTest() {
        val articleId = 489596
        val manager = ArticlesManager.Builder(OkHttpClient()).build()
        val request = VoteArticleRequest(session.clientKey, session.tokenKey, articleId)
        val response = manager.voteUp(request).blockingGet()
        println(response)
    }

    @Test
    @Ignore("Test uses real api")
    fun voteDownArticleTest() {
        val articleId = 489440
        val manager = ArticlesManager.Builder(OkHttpClient()).build()
        val request = VoteArticleRequest(session.clientKey, session.tokenKey, articleId)
        val response = manager.voteDown(request).blockingGet()
        println(response)
    }
}