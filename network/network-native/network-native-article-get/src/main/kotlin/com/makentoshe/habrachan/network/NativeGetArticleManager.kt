package com.makentoshe.habrachan.network

import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.functional.Result
import com.makentoshe.habrachan.functional.fold
import com.makentoshe.habrachan.network.api.NativeArticlesApi
import com.makentoshe.habrachan.network.manager.GetArticleManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class NativeGetArticleManager internal constructor(
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
            deserializer.body(request, it.string())
        }, {
            deserializer.error(request, it.string())
        }).fold({
            Result.success(it.build(request))
        }, { throwable ->
            Result.failure(throwable)
        })
    } catch (exception: Exception) {
        Result.failure(NativeGetArticleException(request, exception.localizedMessage, exception))
    }

    class Builder(private val client: OkHttpClient) {

        private val baseUrl = "https://habr.com/"
        private val deserializer = NativeGetArticleDeserializer()

        private fun getRetrofit() = Retrofit.Builder().client(client).baseUrl(baseUrl).build()

        fun build() = NativeGetArticleManager(getRetrofit().create(NativeArticlesApi::class.java), deserializer)
    }
}