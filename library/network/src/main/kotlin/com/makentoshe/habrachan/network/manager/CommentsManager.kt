package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.network.api.NativeCommentsApi
import com.makentoshe.habrachan.network.converter.CommentsConverter
import com.makentoshe.habrachan.network.converter.VoteCommentConverter
import com.makentoshe.habrachan.network.fold
import com.makentoshe.habrachan.network.request.GetCommentsRequest
import com.makentoshe.habrachan.network.request.VoteCommentRequest
import com.makentoshe.habrachan.network.response.GetCommentsResponse
import com.makentoshe.habrachan.network.response.VoteCommentResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit

interface CommentsManager {

    suspend fun getComments(request: GetCommentsRequest): Result<GetCommentsResponse>

    suspend fun voteUp(request: VoteCommentRequest): Result<VoteCommentResponse>

    suspend fun voteDown(request: VoteCommentRequest): Result<VoteCommentResponse>

    class Factory(private val client: OkHttpClient) {

        private val baseUrl = "https://habr.com/"

        private fun getRetrofit() = Retrofit.Builder().client(client).baseUrl(baseUrl).build()

        fun build() = NativeCommentsManager(getRetrofit().create(NativeCommentsApi::class.java))
    }
}

@Suppress("BlockingMethodInNonBlockingContext")
class NativeCommentsManager(private val api: NativeCommentsApi) : CommentsManager {

    override suspend fun getComments(request: GetCommentsRequest): Result<GetCommentsResponse> {
        return api.getComments(
            request.session.client,
            request.session.token,
            request.session.api,
            request.articleId
        ).execute().fold({
            CommentsConverter().convertBody(it)
        }, {
            CommentsConverter().convertError(it)
        })
    }

    override suspend fun voteUp(request: VoteCommentRequest): Result<VoteCommentResponse> {
        return api.voteUp(request.client, request.token, request.commentId).execute().fold({
            VoteCommentConverter().convertBody(it)
        }, {
            VoteCommentConverter().convertError(it)
        })
    }

    override suspend fun voteDown(request: VoteCommentRequest): Result<VoteCommentResponse> {
        return api.voteDown(request.client, request.token, request.commentId).execute().fold({
            VoteCommentConverter().convertBody(it)
        }, {
            VoteCommentConverter().convertError(it)
        })
    }
}
