package com.makentoshe.habrachan.application.android.database.record

import androidx.room.Entity
import com.makentoshe.habrachan.entity.Hub

@Entity
data class HubRecord(
    val id: Int,
    val title: String,
    val about: String,
    val aboutSmall: String,
    val alias: String,
    val countPosts: Int,
    val countSubscribers: Int,
    val flow: FlowRecord,
    val icon: String,
    val isCompany: Boolean,
    val isMembership: Boolean,
    val isProfiled: Boolean,
    val path: String,
    val rating: Double,
    val tagsString: String
) {
    constructor(hub: Hub) : this(
        hub.id,
        hub.title,
        hub.about,
        hub.aboutSmall,
        hub.alias,
        hub.countPosts,
        hub.countSubscribers,
        FlowRecord(hub.flow),
        hub.icon,
        hub.isCompany,
        hub.isMembership,
        hub.isProfiled,
        hub.path,
        hub.rating,
        hub.tagsString
    )
}