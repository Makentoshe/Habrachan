package com.makentoshe.habrachan.common.model.network

import com.makentoshe.habrachan.common.model.entity.GetPostsBySearchResult
import okhttp3.OkHttpClient
import retrofit2.Retrofit

open class HabrManager(
    protected val api: HabrApi
) {

    fun getPostsBySearch(request: GetPostsBySearchRequest): GetPostsBySearchResult {
        return GetPostsBySearch(api).execute(request)
    }

    companion object {
        fun build(
            client: OkHttpClient = OkHttpClient.Builder().build(),
            baseUrl: String = "https://m.habr.com/"
        ): HabrManager {
            val retrofit = Retrofit.Builder().client(client).baseUrl(baseUrl)
                .addConverterFactory(GetPostsBySearchResultConverterFactory())
                .build()
            return HabrManager(retrofit.create(HabrApi::class.java))
        }
    }
}
