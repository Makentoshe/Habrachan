package com.makentoshe.habrachan.common.network

import com.makentoshe.habrachan.BaseTest
import com.makentoshe.habrachan.ResponseInterceptor
import com.makentoshe.habrachan.UrlInterceptor
import com.makentoshe.habrachan.common.entity.session.ArticlesRequestSpec
import com.makentoshe.habrachan.common.network.manager.ArticlesManager
import com.makentoshe.habrachan.common.network.request.GetArticlesRequest
import com.makentoshe.habrachan.common.network.response.ArticlesResponse
import okhttp3.OkHttpClient
import org.junit.Assert
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

}