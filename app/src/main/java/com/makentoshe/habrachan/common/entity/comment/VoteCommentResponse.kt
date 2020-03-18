package com.makentoshe.habrachan.common.entity.comment

import com.google.gson.annotations.SerializedName

sealed class VoteCommentResponse {

    data class Success(
        @SerializedName("score")
        val score: Int,
        @SerializedName("server_time")
        val serverTime: String
    ) : VoteCommentResponse()

    data class Error(
        @SerializedName("additional")
        val additional: List<String>,
        @SerializedName("code")
        val code: Int,
        @SerializedName("message")
        val message: String
    ) : VoteCommentResponse()
}