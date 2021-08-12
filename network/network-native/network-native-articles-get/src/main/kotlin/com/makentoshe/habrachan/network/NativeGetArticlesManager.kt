package com.makentoshe.habrachan.network

import com.makentoshe.habrachan.functional.Result
import com.makentoshe.habrachan.network.api.NativeArticlesApi
import com.makentoshe.habrachan.network.manager.GetArticlesManager
import com.makentoshe.habrachan.network.request.SpecType
import com.makentoshe.habrachan.network.request.TopSpecType
import com.makentoshe.habrachan.network.response.GetArticlesResponse2
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class NativeGetArticlesManager(
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
        api.getArticles(
            request.session.client, request.session.api, request.session.token, request.spec.path, request.page
        ).execute().fold({
            deserializer.body(request, it.string())
        }, {
            deserializer.error(request, it.string())
        })
    } catch (exception: Exception) {
        Result.failure(exception)
    }

    class Builder(private val client: OkHttpClient) {

        private val baseUrl = "https://habr.com/"
        private val deserializer = NativeGetArticlesDeserializer()

        private fun getRetrofit() = Retrofit.Builder().client(client).baseUrl(baseUrl).build()

        fun build() = NativeGetArticlesManager(getRetrofit().create(NativeArticlesApi::class.java), deserializer)
    }
}
