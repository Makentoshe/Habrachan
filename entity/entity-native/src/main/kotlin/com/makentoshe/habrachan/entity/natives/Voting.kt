package com.makentoshe.habrachan.entity.natives

import com.google.gson.annotations.SerializedName

data class Voting(
    @SerializedName("reasonMap")
    val reasonMap: Any?,
    @SerializedName("score")
    val score: Int,
    @SerializedName("vote")
    val vote: Double?,
    @SerializedName("voteCount")
    val voteCount: VoteCount
)