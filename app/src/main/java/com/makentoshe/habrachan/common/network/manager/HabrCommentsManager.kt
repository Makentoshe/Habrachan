package com.makentoshe.habrachan.common.network.manager

import com.makentoshe.habrachan.common.entity.comment.GetCommentsResponse
import com.makentoshe.habrachan.common.entity.comment.VoteCommentResponse
import com.makentoshe.habrachan.common.network.api.HabrCommentsApi
import com.makentoshe.habrachan.common.network.converter.CommentsConverter
import com.makentoshe.habrachan.common.network.converter.VoteCommentConverter
import com.makentoshe.habrachan.common.network.request.GetCommentsRequest
import com.makentoshe.habrachan.common.network.request.VoteCommentRequest
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit

interface HabrCommentsManager {

    fun getComments(request: GetCommentsRequest): Single<GetCommentsResponse>

    fun voteUp(request: VoteCommentRequest): Single<VoteCommentResponse>

    fun voteDown(request: VoteCommentRequest): Single<VoteCommentResponse>

    class Factory(private val client: OkHttpClient) {

        private val baseUrl = "https://habr.com/"

        private fun getRetrofit() = Retrofit.Builder().client(client).baseUrl(baseUrl).build()

        fun build(): HabrCommentsManager {
            val api = getRetrofit().create(HabrCommentsApi::class.java)
            return object : HabrCommentsManager {
                override fun getComments(request: GetCommentsRequest): Single<GetCommentsResponse> {
                    return Single.just(request).observeOn(Schedulers.io()).map { request ->
                        val response = api.getComments(request.client, request.token, request.api, request.articleId).execute()
                        if (response.isSuccessful) {
                            CommentsConverter().convertBody(response.body()!!)
                        } else {
                            CommentsConverter().convertError(response.errorBody()!!)
                        }
                    }
                }

                override fun voteUp(request: VoteCommentRequest): Single<VoteCommentResponse> {
                    return Single.just(request).observeOn(Schedulers.io()).map { request ->
                        val response = api.voteUp(request.client, request.token, request.commentId).execute()
                        if (response.isSuccessful) {
                            VoteCommentConverter().convertBody(response.body()!!)
                        } else {
                            VoteCommentConverter().convertError(response.errorBody()!!)
                        }
                    }
                }

                override fun voteDown(request: VoteCommentRequest): Single<VoteCommentResponse> {
                    return Single.just(request).observeOn(Schedulers.io()).map { request ->
                        val response = api.voteDown(request.client, request.token, request.commentId).execute()
                        if (response.isSuccessful) {
                            VoteCommentConverter().convertBody(response.body()!!)
                        } else {
                            VoteCommentConverter().convertError(response.errorBody()!!)
                        }
                    }
                }
            }
        }
    }
}