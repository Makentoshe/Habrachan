package com.makentoshe.habrachan.application.android.database.record

import androidx.room.Entity
import com.makentoshe.habrachan.entity.Badge

@Entity
class BadgeRecord(
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
}