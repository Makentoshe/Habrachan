package com.makentoshe.habrachan.entity

import java.text.SimpleDateFormat
import java.util.*

interface User {
    val avatar: String
    val fullname: String
    val login: String

    val speciality: String
//    val gender: Int TODO make enum later
    val timeRegisteredRaw: String
    val rating: Float
    val ratingPosition: Int

    val score: Float
    val postsCount: Int
    val commentsCount: Int
    val favoritesCount: Int
    val followsCount: Int
    val followersCount: Int

    val isReadOnly: Boolean
    val isSubscribed: Boolean
    val isCanVote: Boolean
}

val User.timeRegistered: Date
    get() = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse(timeRegisteredRaw)

fun user(
    login: String,
    fullname: String,
    speciality: String,
    timeRegisteredRaw: String,
    avatar: String,
    rating: Float,
    ratingPosition: Int,
    score: Float,
    postsCount: Int,
    commentsCount: Int,
    favoritesCount: Int,
    followsCount: Int,
    followersCount: Int,
    isReadOnly: Boolean,
    isSubscribe: Boolean,
    isCanVote: Boolean
) = object: User {
    override val login = login
    override val fullname = fullname
    override val speciality = speciality
    override val timeRegisteredRaw = timeRegisteredRaw
    override val avatar = avatar
    override val rating = rating
    override val ratingPosition = ratingPosition
    override val score = score
    override val postsCount = postsCount
    override val commentsCount = commentsCount
    override val favoritesCount = favoritesCount
    override val followersCount = followersCount
    override val followsCount = followsCount
    override val isSubscribed = isSubscribe
    override val isCanVote = isCanVote
    override val isReadOnly = isReadOnly
}