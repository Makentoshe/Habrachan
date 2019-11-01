package com.makentoshe.habrachan.common.network.manager

import com.makentoshe.habrachan.common.entity.post.PostResponse
import com.makentoshe.habrachan.common.network.api.HabrPostsApi
import com.makentoshe.habrachan.common.network.converter.PostConverterFactory
import com.makentoshe.habrachan.common.network.converter.PostsConverterFactory
import com.makentoshe.habrachan.common.network.request.GetPostRequest
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

interface HabrPostManager {

    fun getPost(request: GetPostRequest): Single<PostResponse>

    class Factory(private val client: OkHttpClient) {

        private val baseUrl = "https://habr.com/"

        private fun getRetrofit() = Retrofit.Builder().client(client).baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(PostConverterFactory())
            .build()

        fun build(): HabrPostManager {
            val retrofit = getRetrofit()
            return object : HabrPostManager {
                override fun getPost(request: GetPostRequest): Single<PostResponse> {
                    val api = retrofit.create(HabrPostsApi::class.java)
                    return api.getPost(
                        request.client, request.token, request.api, request.id, null, null, null
                    )
                }
            }
        }
    }
}
