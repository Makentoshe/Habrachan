package com.makentoshe.habrachan.network

import com.makentoshe.habrachan.entity.CommentId
import com.makentoshe.habrachan.entity.CommentVote
import com.makentoshe.habrachan.functional.Result
import com.makentoshe.habrachan.network.api.NativeCommentsApi
import com.makentoshe.habrachan.network.manager.VoteCommentManager
import com.makentoshe.habrachan.network.response.VoteCommentResponse2
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit

class NativeVoteCommentManager internal constructor(
    private val api: NativeCommentsApi, private val deserializer: NativeVoteCommentDeserializer
) : VoteCommentManager<NativeVoteCommentRequest> {

    override fun request(userSession: UserSession, commentId: CommentId, vote: CommentVote): NativeVoteCommentRequest {
        return NativeVoteCommentRequest(userSession, commentId, vote)
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun vote(request: NativeVoteCommentRequest): Result<VoteCommentResponse2> = try {
        when (request.commentVote) {
            is CommentVote.Up -> {
                api.voteUp(request.userSession.client, request.userSession.token, request.commentId.commentId)
            }
            is CommentVote.Down -> {
                api.voteDown(request.userSession.client, request.userSession.token, request.commentId.commentId)
            }
        }.executeAndFold(deserializer, request)
    } catch (exception: Exception) {
        val additional = listOf(exception.localizedMessage)
        Result.failure(NativeVoteCommentException(request, "", additional, 0, exception.localizedMessage, exception))
    }

    private fun Call<ResponseBody>.executeAndFold(
        deserializer: NativeVoteCommentDeserializer, request: NativeVoteCommentRequest
    ) = execute().run {
        fold({
            deserializer.success(request, it.string(), code(), message())
        }, {
            deserializer.failure(request, it.string(), code(), message())
        })
    }

    class Builder(private val client: OkHttpClient) {

        private val deserializer = NativeVoteCommentDeserializer()
        private val baseUrl = "https://habr.com/"

        private fun getRetrofit() = Retrofit.Builder().client(client).baseUrl(baseUrl).build()

        fun build() = NativeVoteCommentManager(getRetrofit().create(NativeCommentsApi::class.java), deserializer)
    }
}
