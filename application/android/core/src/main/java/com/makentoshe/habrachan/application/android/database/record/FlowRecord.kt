package com.makentoshe.habrachan.application.android.database.record

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.entity.ArticleFlow
import com.makentoshe.habrachan.entity.articleFlow
import com.makentoshe.habrachan.entity.natives.Flow

@Entity
data class FlowRecord(
    @PrimaryKey
    @SerializedName("id")
    val id: Int,
    @SerializedName("alias")
    val alias: String,
    @SerializedName("hubs_count")
    val hubsCount: Int?,
    @SerializedName("name")
    val name: String,
    @SerializedName("path")
    val path: String?,
    @SerializedName("url")
    val url: String?
) {
    constructor(flow: Flow) : this(flow.id, flow.alias, flow.hubsCount, flow.name, flow.path, flow.url)

    constructor(flow: ArticleFlow) : this(flow.flowId, flow.alias, null, flow.alias, null, null)

    // TODO move to database controller
    fun toFlow() = Flow(id, name, alias, url, path, hubsCount)

    // TODO move to database controller
    fun toArticleFlow() = articleFlow(id, name, alias)
}