package com.makentoshe.habrachan.application.android.database.cache.record

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.makentoshe.habrachan.entity.natives.Badge

@Entity()
class BadgeRecord(
    @PrimaryKey
    val id: Int,
    val alias: String,
    val description: String,
    val isDisabled: Boolean,
    val isRemovable: Boolean,
    val title: String
) {
    constructor(badge: Badge) : this(
        badge.id,
        badge.alias,
        badge.description,
        badge.isDisabled,
        badge.isRemovable,
        badge.title
    )

    // TODO(high) fix unknown property
    fun toBadge() = Badge(alias, description, id, isDisabled, isRemovable, title, null)
}