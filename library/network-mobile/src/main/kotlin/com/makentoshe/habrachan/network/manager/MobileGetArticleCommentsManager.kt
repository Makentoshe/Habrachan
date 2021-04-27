package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.entity.articleId
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.api.MobileCommentsApi
import com.makentoshe.habrachan.network.fold
import com.makentoshe.habrachan.network.request.MobileGetArticleCommentsRequest
import com.makentoshe.habrachan.network.response.GetArticleCommentsResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class MobileGetArticleCommentsManager(
    private val api: MobileCommentsApi
) : GetArticleCommentsManager<MobileGetArticleCommentsRequest> {
    override fun request(userSession: UserSession, articleId: Int): MobileGetArticleCommentsRequest {
        return MobileGetArticleCommentsRequest(userSession, articleId(articleId))
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun comments(request: MobileGetArticleCommentsRequest): Result<GetArticleCommentsResponse> {
        api.getComments(request.articleId.articleId, request.session.filterLanguage, request.session.habrLanguage).execute().fold({
            println(it.string())
        }, {
            println(it.string())
        })
        return Result.failure(Exception("Stub"))
    }

    class Builder(private val client: OkHttpClient) {

        private val baseUrl = "https://m.habr.com/"

        private fun getRetrofit() = Retrofit.Builder().client(client).baseUrl(baseUrl).build()

        fun build(): MobileGetArticleCommentsManager {
            return MobileGetArticleCommentsManager(getRetrofit().create(MobileCommentsApi::class.java))
        }
    }
}