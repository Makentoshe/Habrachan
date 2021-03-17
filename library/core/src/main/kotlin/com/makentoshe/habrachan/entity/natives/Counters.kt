package com.makentoshe.habrachan.entity.natives


import com.google.gson.annotations.SerializedName

data class Counters(
    @SerializedName("comments")
    val comments: Int, // 8
    @SerializedName("favorites")
    val favorites: Int, // 14
    @SerializedName("followed")
    val followed: Int, // 1
    @SerializedName("followers")
    val followers: Int, // 0
    @SerializedName("posts")
    val posts: Int // 1
)