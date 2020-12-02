package com.makentoshe.habrachan.application.android.database.record

import androidx.room.Entity
import com.makentoshe.habrachan.entity.Flow

@Entity
data class FlowRecord(
    val id: Int,
    val alias: String,
    val hubsCount: Int,
    val name: String,
    val path: String,
    val url: String
) {
    constructor(flow: Flow) : this(
        flow.id, flow.alias, flow.hubsCount, flow.name, flow.path, flow.url
    )
}