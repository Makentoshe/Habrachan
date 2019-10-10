package com.makentoshe.habrachan.common.network.manager

import com.makentoshe.habrachan.common.entity.post.PostResponse
import com.makentoshe.habrachan.common.entity.posts.PostsResponse
import com.makentoshe.habrachan.common.network.api.HabrPostsApi
import com.makentoshe.habrachan.common.network.converter.PostConverterFactory
import com.makentoshe.habrachan.common.network.converter.PostsConverterFactory
import com.makentoshe.habrachan.common.network.request.GetPostRequest
import com.makentoshe.habrachan.common.network.request.GetPostsRequest
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

interface HabrPostsManager {

    fun getPosts(request: GetPostsRequest): Single<PostsResponse>

    fun getPostsWithBody(request: GetPostsRequest): Single<PostsResponse>

    fun getPost(request: GetPostRequest): Single<PostResponse>

    class Builder(private val client: OkHttpClient) {

        private val baseUrl = "https://habr.com/"

        private fun getRetrofit(): Retrofit {
            return Retrofit.Builder().client(client).baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(PostsConverterFactory())
                .addConverterFactory(PostConverterFactory())
                .build()
        }

        fun build(): HabrPostsManager {
            val retrofit = getRetrofit()
            return HabrPostsManagerImpl(retrofit.create(HabrPostsApi::class.java))
        }
    }

}
