package com.makentoshe.habrachan.common.entity.article


import com.google.gson.annotations.SerializedName

data class VoteArticleResponse(
    @SerializedName("ok")
    val ok: Boolean,
    @SerializedName("score")
    val score: Int,
    @SerializedName("server_time")
    val serverTime: String
)