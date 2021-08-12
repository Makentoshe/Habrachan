package com.makentoshe.habrachan.network

import com.makentoshe.habrachan.functional.Result
import com.makentoshe.habrachan.network.api.NativeArticlesApi
import com.makentoshe.habrachan.network.manager.GetArticlesManager
import com.makentoshe.habrachan.network.request.SpecType
import com.makentoshe.habrachan.network.request.TopSpecType
import com.makentoshe.habrachan.network.response.GetArticlesResponse2
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit

class NativeGetArticlesManager internal constructor(
    private val api: NativeArticlesApi, private val deserializer: NativeGetArticlesDeserializer
) : GetArticlesManager<NativeGetArticlesRequest, NativeGetArticlesSpec> {

    override val specs = listOf(
        NativeGetArticlesSpec(SpecType.All, "posts/all"),
        NativeGetArticlesSpec(SpecType.Interesting, "posts/interesting"),
        NativeGetArticlesSpec(SpecType.Top(TopSpecType.Daily), "top/daily"),
        NativeGetArticlesSpec(SpecType.Top(TopSpecType.Weekly), "top/weekly"),
        NativeGetArticlesSpec(SpecType.Top(TopSpecType.Monthly), "top/monthly"),
        NativeGetArticlesSpec(SpecType.Top(TopSpecType.Yearly), "top/yearly"),
        NativeGetArticlesSpec(SpecType.Top(TopSpecType.Alltime), "top/alltime")
    )

    override fun spec(type: SpecType): NativeGetArticlesSpec? {
        return specs.find { it.type == type }
    }

    override fun request(userSession: UserSession, page: Int, spec: NativeGetArticlesSpec): NativeGetArticlesRequest {
        return NativeGetArticlesRequest(userSession, page, spec)
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun articles(request: NativeGetArticlesRequest): Result<GetArticlesResponse2> = try {
        getArticlesApi(request).deserialize(request)
    } catch (exception: Exception) {
        Result.failure(NativeGetArticlesException(request, message = exception.message, cause = exception))
    }

    private fun getArticlesApi(request: NativeGetArticlesRequest) = api.getArticles(
        request.session.client, request.session.api, request.session.token, request.spec.path, request.page
    ).execute()

    private fun Response<ResponseBody>.deserialize(request: NativeGetArticlesRequest): Result<GetArticlesResponse2> {
        return fold({ successResponseBody ->
            deserializer.body(request, successResponseBody.string())
        }, { failureResponseBody ->
            deserializer.error(request, failureResponseBody.string())
        })
    }

    class Builder(private val client: OkHttpClient) {

        private val baseUrl = "https://habr.com/"
        private val deserializer = NativeGetArticlesDeserializer()

        private fun getRetrofit() = Retrofit.Builder().client(client).baseUrl(baseUrl).build()

        fun build() = NativeGetArticlesManager(getRetrofit().create(NativeArticlesApi::class.java), deserializer)
    }
}
