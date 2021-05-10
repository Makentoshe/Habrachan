package com.makentoshe.habrachan.entity.mobile

import com.google.gson.annotations.SerializedName

data class Statistics(
    @SerializedName("commentsCount")
    val commentsCount: Int, // 1
    @SerializedName("favoritesCount")
    val favoritesCount: Int, // 9
    @SerializedName("readingCount")
    val readingCount: Int, // 1623
    @SerializedName("score")
    val score: Int, // 2
    @SerializedName("votesCount")
    val votesCount: Int // 4
)