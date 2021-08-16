package com.makentoshe.habrachan.network

import com.makentoshe.habrachan.entity.articleId
import com.makentoshe.habrachan.functional.Result
import com.makentoshe.habrachan.network.manager.GetArticleCommentsManager
import com.makentoshe.habrachan.network.natives.api.NativeCommentsApi
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit

class NativeGetArticleCommentsManager internal constructor(
    private val api: NativeCommentsApi, private val deserializer: NativeGetArticleCommentsDeserializer
) : GetArticleCommentsManager<NativeGetArticleCommentsRequest> {

    override fun request(userSession: UserSession, articleId: Int): NativeGetArticleCommentsRequest {
        return NativeGetArticleCommentsRequest(userSession, articleId(articleId))
    }

    @Suppress("BlockingMethodInNonBlockingContext") // suspend function
    override suspend fun comments(request: NativeGetArticleCommentsRequest): Result<NativeGetArticleCommentsResponse> =
        try {
            getCommentsApi(request).deserialize(request)
        } catch (exception: Exception) {
            Result.failure(
                NativeGetArticleCommentsException(
                    request,
                    message = exception.localizedMessage,
                    cause = exception
                )
            )
        }

    private fun getCommentsApi(request: NativeGetArticleCommentsRequest): Response<ResponseBody> {
        return api.getComments(
            request.session.client,
            request.session.token,
            request.session.api,
            request.articleId.articleId
        ).execute()
    }

    private fun Response<ResponseBody>.deserialize(request: NativeGetArticleCommentsRequest): Result<NativeGetArticleCommentsResponse> {
        return fold({ successBody ->
            deserializer.body(request, successBody.string())
        }, { failureBody ->
            deserializer.error(request, failureBody.string())
        })
    }

    class Builder(private val client: OkHttpClient) {

        private val baseUrl = "https://habr.com/"
        private val deserializer = NativeGetArticleCommentsDeserializer()

        private fun getRetrofit() = Retrofit.Builder().client(client).baseUrl(baseUrl).build()

        fun build() = NativeGetArticleCommentsManager(getRetrofit().create(NativeCommentsApi::class.java), deserializer)
    }
}