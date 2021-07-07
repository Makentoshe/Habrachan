package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.entity.articleId
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.api.NativeCommentsApi
import com.makentoshe.habrachan.network.deserializer.NativeGetCommentsDeserializer
import com.makentoshe.habrachan.network.fold
import com.makentoshe.habrachan.network.request.NativeGetArticleCommentsRequest
import com.makentoshe.habrachan.network.response.NativeGetArticleCommentsResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class NativeGetArticleCommentsManager(
    private val api: NativeCommentsApi, private val deserializer: NativeGetCommentsDeserializer
) : GetArticleCommentsManager<NativeGetArticleCommentsRequest> {

    override fun request(userSession: UserSession, articleId: Int): NativeGetArticleCommentsRequest {
        return NativeGetArticleCommentsRequest(userSession, articleId(articleId))
    }

    @Suppress("BlockingMethodInNonBlockingContext") // suspend function
    override suspend fun comments(request: NativeGetArticleCommentsRequest) : Result<NativeGetArticleCommentsResponse> = try {
        api.getComments(request.session.client, request.session.token, request.session.api, request.articleId.articleId).execute().fold({
            deserializer.body(request, it.string())
        }, {
            deserializer.error(request, it.string())
        })
    } catch (exception: Exception) {
        Result.failure(exception)
    }

    class Builder(private val client: OkHttpClient, private val deserializer: NativeGetCommentsDeserializer) {

        private val baseUrl = "https://habr.com/"

        private fun getRetrofit() = Retrofit.Builder().client(client).baseUrl(baseUrl).build()

        fun build() = NativeGetArticleCommentsManager(getRetrofit().create(NativeCommentsApi::class.java), deserializer)
    }
}
