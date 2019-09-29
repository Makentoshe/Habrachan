package com.makentoshe.habrachan.common.model.network.votepost


import com.google.gson.annotations.SerializedName

data class VotePostResult(
    @SerializedName("score")
    val score: Int,
    @SerializedName("server_time")
    val serverTime: String,
    @SerializedName("ok")
    val success: Boolean
)