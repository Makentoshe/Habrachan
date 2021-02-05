package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.network.api.MobileArticlesApi
import com.makentoshe.habrachan.network.deserializer.MobileGetArticlesDeserializer
import com.makentoshe.habrachan.network.fold
import com.makentoshe.habrachan.network.request.MobileGetArticlesRequest
import com.makentoshe.habrachan.network.request.MobileGetArticlesSpec
import com.makentoshe.habrachan.network.request.SpecType
import com.makentoshe.habrachan.network.request.TopSpecType
import com.makentoshe.habrachan.network.response.GetArticlesResponse2
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class MobileGetArticlesManager(
    private val api: MobileArticlesApi,
    private val deserializer: MobileGetArticlesDeserializer
): GetArticlesManager<MobileGetArticlesRequest, MobileGetArticlesSpec> {

    override val specs = listOf(
        MobileGetArticlesSpec(SpecType.All, mapOf("sort" to "rating")),
        MobileGetArticlesSpec(SpecType.Top(TopSpecType.Daily), mapOf("sort" to "date", "period" to "daily")),
        MobileGetArticlesSpec(SpecType.Top(TopSpecType.Weekly), mapOf("sort" to "date", "period" to "weekly")),
        MobileGetArticlesSpec(SpecType.Top(TopSpecType.Monthly), mapOf("sort" to "date", "period" to "monthly")),
        MobileGetArticlesSpec(SpecType.Top(TopSpecType.Yearly), mapOf("sort" to "date", "period" to "yearly")),
        MobileGetArticlesSpec(SpecType.Top(TopSpecType.Alltime), mapOf("sort" to "date", "period" to "alltime"))
    )

    override fun spec(type: SpecType): MobileGetArticlesSpec? {
        return specs.find { it.type  == type }
    }

    override fun request(page: Int, spec: MobileGetArticlesSpec) : MobileGetArticlesRequest{
        return MobileGetArticlesRequest(page, spec)
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun articles(request: MobileGetArticlesRequest): Result<GetArticlesResponse2> {
        return api.getArticles(request.page, request.spec.query).execute().fold({ body ->
            deserializer.body(body.string())
        }, { error ->
            deserializer.error(error.string())
        })
    }

    class Builder(private val client: OkHttpClient, private val deserializer: MobileGetArticlesDeserializer) {

        private val baseUrl = "https://m.habr.com/"

        private fun getRetrofit() = Retrofit.Builder().client(client).baseUrl(baseUrl).build()

        fun build() = MobileGetArticlesManager(getRetrofit().create(MobileArticlesApi::class.java), deserializer)
    }
}
