package com.makentoshe.habrachan.application.android.database.record

import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.entity.Flow

@Entity
data class FlowRecord(
    @SerializedName("id")
    val id: Int,
    @SerializedName("alias")
    val alias: String,
    @SerializedName("hubs_count")
    val hubsCount: Int?,
    @SerializedName("name")
    val name: String,
    @SerializedName("path")
    val path: String,
    @SerializedName("url")
    val url: String
) {
    constructor(flow: Flow) : this(flow.id, flow.alias, flow.hubsCount, flow.name, flow.path, flow.url)

    fun toFlow() = Flow(id, name, alias, url, path, hubsCount)
}