package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.network.api.NativeCommentsApi
import com.makentoshe.habrachan.network.converter.ConverterException
import com.makentoshe.habrachan.network.converter.convertCommentsSuccess
import com.makentoshe.habrachan.network.fold
import com.makentoshe.habrachan.network.request.GetCommentsRequest
import com.makentoshe.habrachan.network.response.GetCommentsResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit

interface CommentsManager {

    suspend fun getComments(request: GetCommentsRequest): Result<GetCommentsResponse>

//    suspend fun voteUp(request: VoteCommentRequest): Result<VoteCommentResponse>
//
//    suspend fun voteDown(request: VoteCommentRequest): Result<VoteCommentResponse>

    class Factory(private val client: OkHttpClient) {

        private val baseUrl = "https://habr.com/"

        private fun getRetrofit() = Retrofit.Builder().client(client).baseUrl(baseUrl).build()

        fun native() = NativeCommentsManager(getRetrofit().create(NativeCommentsApi::class.java))
    }
}

@Suppress("BlockingMethodInNonBlockingContext")
class NativeCommentsManager(private val api: NativeCommentsApi) : CommentsManager {

    override suspend fun getComments(request: GetCommentsRequest): Result<GetCommentsResponse> {
        val call = api.getComments(request.session.client, request.session.token, request.session.api, request.articleId)
        return call.execute().fold({
            Result.success(convertCommentsSuccess(it.string()).get(request))
        }, {
            Result.failure<GetCommentsResponse>(ConverterException(it.string()))
        })
    }

//    override suspend fun voteUp(request: VoteCommentRequest): Result<VoteCommentResponse> {
//        return api.voteUp(request.client, request.token, request.commentId).execute().fold({
//            VoteCommentConverter().convertBody(it)
//        }, {
//            VoteCommentConverter().convertError(it)
//        })
//    }
//
//    override suspend fun voteDown(request: VoteCommentRequest): Result<VoteCommentResponse> {
//        return api.voteDown(request.client, request.token, request.commentId).execute().fold({
//            VoteCommentConverter().convertBody(it)
//        }, {
//            VoteCommentConverter().convertError(it)
//        })
//    }
}

// TODO move to network api tests
//fun main() : Unit = runBlocking {
//    val userSession = userSession("85cab69095196f3.89453480", "173984950848a2d27c0cc1c76ccf3d6d3dc8255b")
//    val commentsRequest = GetCommentsRequest(userSession, 442440)
//    val manager = CommentsManager.Factory(OkHttpClient()).native()
//    val response = manager.getComments(commentsRequest)
//    println(response)
//}
