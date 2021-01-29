package com.makentoshe.habrachan.application.android.database.record

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.application.android.database.dao.FlowDao
import com.makentoshe.habrachan.entity.ArticleHub
import com.makentoshe.habrachan.entity.articleHub
import com.makentoshe.habrachan.entity.natives.Hub

@Entity
data class HubRecord(
    @PrimaryKey
    @SerializedName("id")
    val hubId: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("alias")
    val alias: String,
    @SerializedName("flow")
    val flowId: Int?,
    @SerializedName("icon")
    val icon: String?,
    @SerializedName("is_company")
    val isCompany: Boolean?,
    @SerializedName("is_membership")
    val isMembership: Boolean?,
    @SerializedName("is_profiled")
    val isProfiled: Boolean?,
    @SerializedName("path")
    val path: String?,
    @SerializedName("about")
    val about: String?,
    @SerializedName("about_small")
    val aboutSmall: String?,
    @SerializedName("count_posts")
    val countPosts: Int?,
    @SerializedName("count_subscribers")
    val countSubscribers: Int?,
    @SerializedName("rating")
    val rating: Double?,
    @SerializedName("tags_string")
    val tagsString: String?
) {
    constructor(hub: Hub) : this(
        hub.id,
        hub.title,
        hub.alias,
        hub.flow?.id,
        hub.icon,
        hub.isCompany,
        hub.isMembership,
        hub.isProfiled,
        hub.path,
        hub.about,
        hub.aboutSmall,
        hub.countPosts,
        hub.countSubscribers,
        hub.rating,
        hub.tagsString
    )

    constructor(hub: ArticleHub) : this(
        hub.hubId,
        hub.title,
        hub.alias,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null
    )

    // TODO move to database controller
    fun toHub(flowDao: FlowDao) = Hub(
        hubId,
        alias,
        title,
        icon,
        isCompany,
        isMembership,
        isProfiled,
        path,
        about,
        aboutSmall,
        countPosts,
        countSubscribers,
        flowId?.let(flowDao::getById)?.toFlow(),
        rating,
        tagsString
    )

    // TODO move to database controller
    fun toArticleHub(): ArticleHub = articleHub(hubId, title, alias)
}
