package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.entity.articleId
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.api.NativeCommentsApi
import com.makentoshe.habrachan.network.deserializer.NativeGetCommentsDeserializer
import com.makentoshe.habrachan.network.fold
import com.makentoshe.habrachan.network.request.GetCommentsRequest2
import com.makentoshe.habrachan.network.request.NativeGetCommentsRequest
import com.makentoshe.habrachan.network.response.NativeGetCommentsResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class NativeGetCommentsManager(
    private val api: NativeCommentsApi, private val deserializer: NativeGetCommentsDeserializer
) : GetCommentsManager<NativeGetCommentsRequest> {

    override fun request(userSession: UserSession, articleId: Int): NativeGetCommentsRequest {
        return NativeGetCommentsRequest(userSession, articleId(articleId))
    }

    override fun comments(request: NativeGetCommentsRequest) : Result<NativeGetCommentsResponse> {
        return api.getComments(request.session.client, request.session.token, request.session.api, request.articleId.articleId).execute().fold({
            deserializer.body(request, it.string())
        }, {
            deserializer.error(request, it.string())
        })
    }

    class Builder(private val client: OkHttpClient, private val deserializer: NativeGetCommentsDeserializer) {

        private val baseUrl = "https://habr.com/"

        private fun getRetrofit() = Retrofit.Builder().client(client).baseUrl(baseUrl).build()

        fun build() = NativeGetCommentsManager(getRetrofit().create(NativeCommentsApi::class.java), deserializer)
    }
}
