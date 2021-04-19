package com.makentoshe.habrachan.application.android.database.record

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.makentoshe.habrachan.entity.User
import com.makentoshe.habrachan.entity.user

@Entity
data class UserRecord(
    val avatar: String,
    val fullname: String,
    @PrimaryKey
    val login: String,
    val speciality: String,
//    val gender: Int TODO make enum later
    val timeRegisteredRaw: String,
    val rating: Float,
    val ratingPosition: Int,
    val score: Float,
    val postsCount: Int,
    val commentsCount: Int,
    val favoritesCount: Int,
    val followsCount: Int,
    val followersCount: Int,
    val isReadOnly: Boolean,
    val isSubscribed: Boolean,
    val isCanVote: Boolean
) {
    constructor(user: User) : this(
        user.avatar,
        user.fullname,
        user.login,
        user.speciality,
        user.timeRegisteredRaw,
        user.rating,
        user.ratingPosition,
        user.score,
        user.postsCount,
        user.commentsCount,
        user.favoritesCount,
        user.followsCount,
        user.followersCount,
        user.isReadOnly,
        user.isSubscribed,
        user.isCanVote
    )

    fun toUser() = user(
        login,
        fullname,
        speciality,
        timeRegisteredRaw,
        avatar,
        rating,
        ratingPosition,
        score,
        postsCount,
        commentsCount,
        favoritesCount,
        followsCount,
        followersCount,
        isReadOnly,
        isSubscribed,
        isCanVote
    )
}