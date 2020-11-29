package com.makentoshe.habrachan.common.entity


import com.google.gson.annotations.SerializedName

data class VoteCount(
    @SerializedName("negative")
    val negative: Int,
    @SerializedName("positive")
    val positive: Int,
    @SerializedName("total")
    val total: Int
)