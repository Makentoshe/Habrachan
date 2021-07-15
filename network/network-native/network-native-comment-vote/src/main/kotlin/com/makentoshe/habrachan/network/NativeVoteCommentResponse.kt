package com.makentoshe.habrachan.network

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.network.request.VoteCommentRequest2
import com.makentoshe.habrachan.network.response.VoteCommentResponse2

class NativeVoteCommentResponse(
    override val request: VoteCommentRequest2, override val score: Int, val serverTime: String
) : VoteCommentResponse2 {

    class Factory(
        @SerializedName("score")
        val score: Int,
        @SerializedName("server_time")
        val serverTime: String
    ) {
        fun build(request: NativeVoteCommentRequest) = NativeVoteCommentResponse(
            request, score, serverTime
        )
    }
}