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

    fun getPost(request: GetPostRequest): Single<PostResponse>

    class Builder(private val client: OkHttpClient) {

        private val baseUrl = "https://habr.com/"

        private fun getRetrofit() = Retrofit.Builder().client(client).baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(PostsConverterFactory())
            .addConverterFactory(PostConverterFactory())
            .build()

        fun build(include: String? = null): HabrPostsManager {
            val retrofit = getRetrofit()
            val api = retrofit.create(HabrPostsApi::class.java)
            return object: HabrPostsManager {
                override fun getPosts(request: GetPostsRequest): Single<PostsResponse> {
                    return api.getPosts(
                        request.client, request.token, request.api, request.path1, request.path2, request.page, include
                    )
                }

                override fun getPost(request: GetPostRequest): Single<PostResponse> {
                    return api.getPost(
                        request.client, request.token, request.api, request.id, null, null, null
                    )
                }
            }
        }
    }
}
