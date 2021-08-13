package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.entity.articleId
import com.makentoshe.habrachan.functional.Result
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.api.MobileCommentsApi
import com.makentoshe.habrachan.network.deserializer.MobileGetArticleCommentsDeserializer
import com.makentoshe.habrachan.network.exceptions.MobileGetArticleCommentsManagerException
import com.makentoshe.habrachan.network.fold
import com.makentoshe.habrachan.network.request.MobileGetArticleCommentsRequest
import com.makentoshe.habrachan.network.response.GetArticleCommentsResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class MobileGetArticleCommentsManager(
    private val api: MobileCommentsApi, private val deserializer: MobileGetArticleCommentsDeserializer
) : GetArticleCommentsManager<MobileGetArticleCommentsRequest> {
    override fun request(userSession: UserSession, articleId: Int): MobileGetArticleCommentsRequest {
        return MobileGetArticleCommentsRequest(userSession, articleId(articleId))
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun comments(request: MobileGetArticleCommentsRequest): Result<GetArticleCommentsResponse> = try {
        api.getComments(request.articleId.articleId, request.session.filterLanguage, request.session.habrLanguage).execute().fold({
            deserializer.body(request, it.string())
        }, {
            deserializer.error(request, it.string())
        })
    } catch (exception: Exception) {
        Result.failure(MobileGetArticleCommentsManagerException(request, exception))
    }

    class Builder(private val client: OkHttpClient, private val deserializer: MobileGetArticleCommentsDeserializer) {

        private val baseUrl = "https://m.habr.com/"

        private fun getRetrofit() = Retrofit.Builder().client(client).baseUrl(baseUrl).build()

        fun build(): MobileGetArticleCommentsManager {
            return MobileGetArticleCommentsManager(getRetrofit().create(MobileCommentsApi::class.java), deserializer)
        }
    }
}