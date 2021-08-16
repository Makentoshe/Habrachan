package com.makentoshe.habrachan.network

import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.functional.Result
import com.makentoshe.habrachan.functional.map
import com.makentoshe.habrachan.network.manager.GetArticleManager
import com.makentoshe.habrachan.network.natives.api.NativeArticlesApi
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit

class NativeGetArticleManager internal constructor(
    private val api: NativeArticlesApi, private val deserializer: NativeGetArticleDeserializer
) : GetArticleManager<NativeGetArticleRequest> {

    override fun request(userSession: UserSession, articleId: ArticleId): NativeGetArticleRequest {
        return NativeGetArticleRequest(userSession, articleId)
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun article(request: NativeGetArticleRequest): Result<NativeGetArticleResponse> = try {
        getArticleApi(request).deserialize(request)
    } catch (exception: Exception) {
        Result.failure(NativeGetArticleException(request, exception.localizedMessage, exception))
    }

    private fun getArticleApi(request: NativeGetArticleRequest) = api.getArticle(
        request.userSession.client, request.userSession.api, request.userSession.token, request.articleId.articleId
    ).execute()

    private fun Response<ResponseBody>.deserialize(request: NativeGetArticleRequest) = fold({ successBody ->
        deserializer.body(request, successBody.string())
    }, { failureBody ->
        deserializer.error(request, failureBody.string())
    }).map { factory -> factory.build(request) }

    class Builder(private val client: OkHttpClient) {

        private val baseUrl = "https://habr.com/"
        private val deserializer = NativeGetArticleDeserializer()

        private fun getRetrofit() = Retrofit.Builder().client(client).baseUrl(baseUrl).build()

        fun build() = NativeGetArticleManager(getRetrofit().create(NativeArticlesApi::class.java), deserializer)
    }
}