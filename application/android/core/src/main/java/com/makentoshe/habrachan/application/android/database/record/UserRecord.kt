package com.makentoshe.habrachan.application.android.database.record

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.makentoshe.habrachan.application.android.database.dao.BadgeDao
import com.makentoshe.habrachan.entity.ArticleAuthor
import com.makentoshe.habrachan.entity.articleAuthor
import com.makentoshe.habrachan.entity.natives.Badge
import com.makentoshe.habrachan.entity.natives.Counters
import com.makentoshe.habrachan.entity.natives.Geo
import com.makentoshe.habrachan.entity.natives.User

@Entity
data class UserRecord(
    @PrimaryKey
    val id: Int,
    val avatar: String,
    /** List of badges ids joined as string */
    // TODO make Relation
    val badges: String?,
    @Embedded(prefix = "counters_")
    val counters: Counters?,
    val fullname: String?,
    @Embedded(prefix = "geo_")
    val geo: Geo?,
    val isCanVote: Boolean?,
    val isRc: Boolean?,
    val isReadonly: Boolean?,
    val isSubscribed: Boolean?,
    val login: String,
    val path: String?,
    val rating: Float?,
    val ratingPosition: Int?,
    val score: Float?,
    val sex: String?,
    val specializm: String?,
    val timeRegistered: String?
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

    constructor(author: ArticleAuthor) : this(
        author.userId,
        author.avatar,
        null,
        null,
        author.fullname,
        null,
        null,
        null,
        null,
        null,
        author.login,
        null,
        null,
        null,
        null,
        null,
        null,
        null
    )

    private fun badges(badgeDao: BadgeDao): List<Badge> {
        if (badges == null || badges.isEmpty()) return emptyList()

        return badges.split(delimiter).mapNotNull {
            val id = it.toIntOrNull() ?: return@mapNotNull null
            badgeDao.getById(id)?.toBadge()
        }
    }

    fun toUser(badgeDao: BadgeDao) = User(
        avatar,
        badges(badgeDao),
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

    fun toArticleAuthor() = articleAuthor(id, avatar, login, fullname)
}