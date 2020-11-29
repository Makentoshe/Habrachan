package com.makentoshe.habrachan.network.response

import com.google.gson.annotations.SerializedName

data class VoteCommentResponse(
    @SerializedName("score")
    val score: Int,
    @SerializedName("server_time")
    val serverTime: String
)