package com.makentoshe.habrachan.application.android.database.record

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.makentoshe.habrachan.application.android.database.dao.BadgeDao
import com.makentoshe.habrachan.entity.Counters
import com.makentoshe.habrachan.entity.Geo
import com.makentoshe.habrachan.entity.User

@Entity
data class UserRecord(
    @PrimaryKey
    val id: String,
    val avatar: String,
    /** List of badges ids joined as string */
    // TODO mb add type converter
    val badges: String,
    @Embedded(prefix = "counters_")
    val counters: Counters,
    val fullname: String? = null,
    @Embedded(prefix = "geo_")
    val geo: Geo?,
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

    companion object {
        private const val delimiter = ";"
    }

    constructor(user: User) : this(
        user.id,
        user.avatar,
        user.badges.joinToString(delimiter) { it.id.toString() },
        user.counters,
        user.fullname,
        user.geo,
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

    fun toUser(badgeDao: BadgeDao) = User(
        avatar,
        badges.split(delimiter).mapNotNull { badgeDao.getById(it.toInt())?.toBadge() },
        counters,
        fullname,
        geo,
        id,
        isCanVote,
        isRc,
        isReadonly,
        isSubscribed,
        login,
        path,
        rating,
        ratingPosition,
        score,
        sex,
        specializm,
        timeRegistered
    )
}