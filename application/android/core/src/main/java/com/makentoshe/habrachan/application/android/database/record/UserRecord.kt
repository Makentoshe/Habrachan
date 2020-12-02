package com.makentoshe.habrachan.application.android.database.record

import androidx.room.Entity
import com.makentoshe.habrachan.entity.User

@Entity
data class UserRecord(
    val id: String,
    val avatar: String,
    val badges: BadgesRecord,
    val counters: CountersRecord,
    val fullname: String? = null,
    val geo: GeoRecord,
    val isCanVote: Boolean,
    val isRc: Boolean,
    val isReadonly: Boolean,
    val isSubscribed: Boolean? = null,
    val login: String,
    val path: String,
    val rating: Float,
    val ratingPosition: Int,
    val score: Float,
    val sex: String,
    val specializm: String? = null,
    val timeRegistered: String
) {
    constructor(user: User) : this(
        user.id,
        user.avatar,
        BadgesRecord().apply { addAll(user.badges.map(::BadgeRecord)) },
        CountersRecord(user.counters),
        user.fullname,
        user.geo?.let(::GeoRecord) ?: GeoRecord(),
        user.isCanVote,
        user.isRc,
        user.isReadonly,
        user.isSubscribed,
        user.login,
        user.path,
        user.rating,
        user.ratingPosition,
        user.score,
        user.sex,
        user.specializm,
        user.timeRegistered
    )
}