package com.makentoshe.habrachan.entity.mobile


import com.google.gson.annotations.SerializedName

data class FollowStats(
    @SerializedName("followCount")
    val followCount: Int, // 1
    @SerializedName("followersCount")
    val followersCount: Int // 0
)