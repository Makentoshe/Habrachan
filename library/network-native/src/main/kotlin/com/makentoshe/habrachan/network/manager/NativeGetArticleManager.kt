package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.api.NativeArticlesApi
import com.makentoshe.habrachan.network.deserializer.NativeGetArticleDeserializer
import com.makentoshe.habrachan.network.fold
import com.makentoshe.habrachan.network.request.NativeGetArticleRequest
import com.makentoshe.habrachan.network.response.NativeGetArticleResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class NativeGetArticleManager(
    private val api: NativeArticlesApi, private val deserializer: NativeGetArticleDeserializer
) : GetArticleManager<NativeGetArticleRequest> {

    override fun request(userSession: UserSession, articleId: ArticleId): NativeGetArticleRequest {
        return NativeGetArticleRequest(userSession, articleId)
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun article(request: NativeGetArticleRequest): Result<NativeGetArticleResponse> = try {
        api.getArticle(
            request.userSession.client, request.userSession.api, request.userSession.token, request.articleId.articleId
        ).execute().fold({
            deserializer.body(it.string())
        }, {
            deserializer.body(it.string())
        }).fold({
            Result.success(it.build(request))
        }, {
            Result.failure(it)
        })
    } catch (exception: Exception) {
        Result.failure(exception)
    }

    class Builder(private val client: OkHttpClient, private val deserializer: NativeGetArticleDeserializer) {

        private val baseUrl = "https://habr.com/"

        private fun getRetrofit() = Retrofit.Builder().client(client).baseUrl(baseUrl).build()

        fun build() = NativeGetArticleManager(getRetrofit().create(NativeArticlesApi::class.java), deserializer)
    }
}