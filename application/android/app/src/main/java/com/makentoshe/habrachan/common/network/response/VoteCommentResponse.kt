package com.makentoshe.habrachan.common.network.response

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.common.network.request.VoteCommentRequest

sealed class VoteCommentResponse {

    data class Success(
        @SerializedName("score")
        val score: Int,
        @SerializedName("server_time")
        val serverTime: String,
        val request: VoteCommentRequest
    ) : VoteCommentResponse()

    data class Error(
        @SerializedName("additional")
        val additional: List<String>,
        @SerializedName("code")
        val code: Int,
        @SerializedName("message")
        val message: String,
        val request: VoteCommentRequest
    ) : VoteCommentResponse()
}