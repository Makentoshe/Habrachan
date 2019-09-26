package com.makentoshe.habrachan.common.model.network.votepost


import com.google.gson.annotations.SerializedName

data class VotePostResult(
    @SerializedName("score")
    val score: Int? = null,
    @SerializedName("server_time")
    val serverTime: String? = null,
    @SerializedName("additional")
    val additional: List<String>? = null,
    @SerializedName("code")
    val code: Int? = null,
    @SerializedName("data")
    val data: Any? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("ok")
    val success: Boolean = code == 200
)