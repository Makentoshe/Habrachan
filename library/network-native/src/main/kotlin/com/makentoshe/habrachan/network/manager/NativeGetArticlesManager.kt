package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.api.NativeArticlesApi
import com.makentoshe.habrachan.network.deserializer.NativeGetArticlesDeserializer
import com.makentoshe.habrachan.network.fold
import com.makentoshe.habrachan.network.request.NativeGetArticlesRequest
import com.makentoshe.habrachan.network.request.NativeGetArticlesSpec
import com.makentoshe.habrachan.network.request.SpecType
import com.makentoshe.habrachan.network.request.TopSpecType
import com.makentoshe.habrachan.network.response.GetArticlesResponse2
import okhttp3.OkHttpClient
import retrofit2.Retrofit

// TODO replace by UserSessionProvider
class NativeGetArticlesManager(
    private val api: NativeArticlesApi,
    private val deserializer: NativeGetArticlesDeserializer
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
    override suspend fun articles(request: NativeGetArticlesRequest): Result<GetArticlesResponse2> {
        return api.getArticles(
            request.session.client, request.session.api, request.session.token, request.spec.path, request.page
        ).execute().fold({
            deserializer.body(it.string())
        }, {
            deserializer.error(it.string())
        })
    }

    class Builder(private val client: OkHttpClient, private val deserializer: NativeGetArticlesDeserializer) {

        private val baseUrl = "https://habr.com/"

        private fun getRetrofit() = Retrofit.Builder().client(client).baseUrl(baseUrl).build()

        fun build() =
            NativeGetArticlesManager(getRetrofit().create(NativeArticlesApi::class.java), deserializer)
    }
}
