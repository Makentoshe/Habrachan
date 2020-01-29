package com.makentoshe.habrachan.common.network.manager

import com.makentoshe.habrachan.common.entity.comment.CommentsResponse
import com.makentoshe.habrachan.common.entity.post.PostResponse
import com.makentoshe.habrachan.common.network.api.HabrCommentsApi
import com.makentoshe.habrachan.common.network.api.HabrPostsApi
import com.makentoshe.habrachan.common.network.converter.CommentsConverterFactory
import com.makentoshe.habrachan.common.network.converter.PostConverterFactory
import com.makentoshe.habrachan.common.network.request.GetCommentsRequest
import com.makentoshe.habrachan.common.network.request.GetPostRequest
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

interface HabrCommentsManager {

    fun getComments(request: GetCommentsRequest): Single<CommentsResponse>

    class Factory(private val client: OkHttpClient) {
        private val baseUrl = "https://habr.com/"

        private fun getRetrofit() = Retrofit.Builder().client(client).baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(CommentsConverterFactory())
            .build()

        fun build(): HabrCommentsManager {
            val retrofit = getRetrofit()
            return object : HabrCommentsManager {
                override fun getComments(request: GetCommentsRequest): Single<CommentsResponse> {
                    val api = retrofit.create(HabrCommentsApi::class.java)
                    return api.getComments(request.client, request.token, request.api, request.articleId, request.since)
                }
            }
        }
    }
}