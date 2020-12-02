package com.makentoshe.habrachan.application.android.database.record

import androidx.room.Entity
import com.makentoshe.habrachan.entity.Counters

@Entity
data class CountersRecord(
    val comments: Int,
    val favorites: Int,
    val followed: Int,
    val followers: Int,
    val posts: Int
) {
    constructor(counters: Counters): this(
        counters.comments,
        counters.favorites,
        counters.followed,
        counters.followers,
        counters.posts
    )
}