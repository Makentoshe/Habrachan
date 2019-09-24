package com.makentoshe.habrachan.common.model.entity


import com.google.gson.annotations.SerializedName

data class Counters(
    @SerializedName("comments")
    val comments: String,
    @SerializedName("favorites")
    val favorites: String,
    @SerializedName("followed")
    val followed: String,
    @SerializedName("followers")
    val followers: String,
    @SerializedName("posts")
    val posts: String
)