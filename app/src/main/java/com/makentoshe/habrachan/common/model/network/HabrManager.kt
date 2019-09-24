package com.makentoshe.habrachan.common.model.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit

open class HabrManager(
    protected val api: HabrApi
) {

    fun getPostsBySearch(request: GetPostsBySearchRequest): Any {
        return GetPostsBySearch(api).execute(request)
    }

    companion object {
        fun build(): HabrManager {
            val client = OkHttpClient.Builder().build()
            val retrofit = Retrofit.Builder().client(client).baseUrl("https://m.habr.com/")
                .addConverterFactory(GetPostsBySearchResultConverterFactory())
                .build()
            return HabrManager(retrofit.create(HabrApi::class.java))
        }
    }
}
