package com.makentoshe.habrachan.application.android.database.record

import androidx.room.Embedded
import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.entity.Hub

@Entity
data class HubRecord(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("about")
    val about: String,
    @SerializedName("about_small")
    val aboutSmall: String,
    @SerializedName("alias")
    val alias: String,
    @SerializedName("count_posts")
    val countPosts: Int,
    @SerializedName("count_subscribers")
    val countSubscribers: Int,
    @Embedded(prefix = "flow")
    @SerializedName("flow")
    val flowRecord: FlowRecord,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("is_company")
    val isCompany: Boolean,
    @SerializedName("is_membership")
    val isMembership: Boolean,
    @SerializedName("is_profiled")
    val isProfiled: Boolean,
    @SerializedName("path")
    val path: String,
    @SerializedName("rating")
    val rating: Double,
    @SerializedName("tags_string")
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

    fun toHub() = Hub(
        about,
        aboutSmall,
        alias,
        countPosts,
        countSubscribers,
        flowRecord.toFlow(),
        icon,
        id,
        isCompany,
        isMembership,
        isProfiled,
        path,
        rating,
        tagsString,
        title
    )
}