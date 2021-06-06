package com.makentoshe.habrachan.entity.mobiles.login


import com.google.gson.annotations.SerializedName

data class ScoreStats(
    @SerializedName("score")
    val score: Int, // 9
    @SerializedName("votesCount")
    val votesCount: Int // 9
)