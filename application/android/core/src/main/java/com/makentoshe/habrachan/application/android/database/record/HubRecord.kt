package com.makentoshe.habrachan.application.android.database.record

import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.entity.Hub

@Entity
data class HubRecord(
    @SerializedName("id")
    val id: Int,
    @SerializedName("alias")
    val alias: String,
    @SerializedName("title")
    val title: String,
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
    @SerializedName("about")
    val about: String?,
    @SerializedName("about_small")
    val aboutSmall: String?,
    @SerializedName("count_posts")
    val countPosts: Int?,
    @SerializedName("count_subscribers")
    val countSubscribers: Int?,
    @SerializedName("flow")
    val flowRecord: FlowRecord?,
    @SerializedName("rating")
    val rating: Double?,
    @SerializedName("tags_string")
    val tagsString: String?
) {
    constructor(hub: Hub) : this(
        hub.id,
        hub.title,
        hub.alias,
        hub.icon,
        hub.isCompany,
        hub.isMembership,
        hub.isProfiled,
        hub.path,
        hub.about,
        hub.aboutSmall,
        hub.countPosts,
        hub.countSubscribers,
        hub.flow?.let(::FlowRecord),
        hub.rating,
        hub.tagsString
    )

    fun toHub() = Hub(
        id,
        title,
        alias,
        icon,
        isCompany,
        isMembership,
        isProfiled,
        path,
        about,
        aboutSmall,
        countPosts,
        countSubscribers,
        flowRecord?.toFlow(),
        rating,
        tagsString
    )
}