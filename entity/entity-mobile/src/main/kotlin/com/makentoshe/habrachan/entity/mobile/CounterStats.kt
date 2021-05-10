package com.makentoshe.habrachan.entity.mobile


import com.google.gson.annotations.SerializedName

data class CounterStats(
    @SerializedName("commentCount")
    val commentCount: Int, // 8
    @SerializedName("favoriteCount")
    val favoriteCount: Int, // 14
    @SerializedName("postCount")
    val postCount: Int // 1
)