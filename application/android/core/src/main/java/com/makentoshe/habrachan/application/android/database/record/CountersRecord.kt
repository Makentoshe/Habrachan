package com.makentoshe.habrachan.application.android.database.record

import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.entity.Counters

@Entity
data class CountersRecord(
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
) {
    constructor(counters: Counters) : this(
        counters.comments, counters.favorites, counters.followed, counters.followers, counters.posts
    )

    fun toCounters() = Counters(comments, favorites, followed, followers, posts)
}