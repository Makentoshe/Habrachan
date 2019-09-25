package com.makentoshe.habrachan.common.model.network

import com.makentoshe.habrachan.common.model.network.posts.GetPostsResult
import com.makentoshe.habrachan.common.model.network.posts.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit

open class HabrManager(
    protected val api: HabrApi
) {

    fun getPostsBySearch(request: GetPostsBySearchRequest): GetPostsResult {
        return GetPostsBySearch(api).execute(request)
    }

    fun getPosts(request: GetPostsRequest): GetPostsResult {
        return GetPosts(api).execute(request)
    }

    companion object {
        fun build(
            client: OkHttpClient = OkHttpClient.Builder().build(),
            baseUrl: String = "https://m.habr.com/"
        ): HabrManager {
            val retrofit = Retrofit.Builder().client(client).baseUrl(baseUrl)
                .addConverterFactory(PostsConverterFactory())
                .build()
            return HabrManager(retrofit.create(HabrApi::class.java))
        }
    }
}
