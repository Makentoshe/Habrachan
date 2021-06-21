package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.api.MobileArticlesApi
import com.makentoshe.habrachan.network.deserializer.MobileGetArticleDeserializer
import com.makentoshe.habrachan.network.fold
import com.makentoshe.habrachan.network.request.MobileGetArticleRequest
import com.makentoshe.habrachan.network.response.MobileGetArticleResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class MobileGetArticleManager(
    private val api: MobileArticlesApi, private val deserializer: MobileGetArticleDeserializer
) : GetArticleManager<MobileGetArticleRequest> {

    override fun request(userSession: UserSession, articleId: ArticleId): MobileGetArticleRequest {
        return MobileGetArticleRequest(userSession, articleId)
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun article(request: MobileGetArticleRequest): Result<MobileGetArticleResponse> = try {
        api.getArticle(
            request.articleId.articleId, request.userSession.filterLanguage, request.userSession.habrLanguage
        ).execute().fold({
            deserializer.body(it.string())
        }, {
            deserializer.error(it.string())
        }).fold({
            Result.success(MobileGetArticleResponse(request, it))
        }, {
            Result.failure(it)
        })
    } catch (exception: Exception) {
        Result.failure(exception)
    }

    class Builder(private val client: OkHttpClient, private val deserializer: MobileGetArticleDeserializer) {

        private val baseUrl = "https://m.habr.com/"

        private fun getRetrofit() = Retrofit.Builder().client(client).baseUrl(baseUrl).build()

        fun build() = MobileGetArticleManager(getRetrofit().create(MobileArticlesApi::class.java), deserializer)
    }
}