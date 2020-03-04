package com.makentoshe.habrachan.common.entity


import com.google.gson.annotations.SerializedName

data class Voting(
//    @SerializedName("reasonMap")
//    val reasonMap: Any?,
    @SerializedName("score")
    val score: Int,
//    @SerializedName("vote")
//    val vote: Any?,
    @SerializedName("voteCount")
    val voteCount: VoteCount
)