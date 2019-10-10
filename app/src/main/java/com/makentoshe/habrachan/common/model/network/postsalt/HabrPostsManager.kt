package com.makentoshe.habrachan.common.model.network.postsalt

import com.makentoshe.habrachan.common.entity.post.PostResponse
import com.makentoshe.habrachan.common.entity.posts.PostsResponse
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

interface HabrPostsManager {

    fun getPosts(request: GetPostsRequest): Single<PostsResponse>

    fun getPostsWithBody(request: GetPostsRequest): Single<PostsResponse>

    fun getPost(request: GetPostRequest): Single<PostResponse>

    companion object {
        fun build(
            client: OkHttpClient = OkHttpClient.Builder().build(),
            baseUrl: String = "https://habr.com/"
        ): HabrPostsManager {
            val retrofit = Retrofit.Builder().client(client).baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(PostsConverterFactory())
                .addConverterFactory(PostConverterFactory())
                .build()
            return HabrPostsManagerImpl(retrofit.create(HabrPostsApi::class.java))
        }
    }
}
