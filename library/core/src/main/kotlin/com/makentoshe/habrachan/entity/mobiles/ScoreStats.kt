package com.makentoshe.habrachan.entity.mobiles


import com.google.gson.annotations.SerializedName

data class ScoreStats(
    @SerializedName("score")
    val score: Float, // 7
    @SerializedName("votesCount")
    val votesCount: Int // 7
)