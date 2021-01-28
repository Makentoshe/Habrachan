package com.makentoshe.habrachan.entity


import com.google.gson.annotations.SerializedName

data class Counters(
    @SerializedName("comments")
    val comments: Int,
    @SerializedName("favorites")
    val favorites: Int,
    @SerializedName("followed")
    val followed: Int,
    @SerializedName("followers")
    val followers: Int,
    @SerializedName("posts")
    val posts: Int
)