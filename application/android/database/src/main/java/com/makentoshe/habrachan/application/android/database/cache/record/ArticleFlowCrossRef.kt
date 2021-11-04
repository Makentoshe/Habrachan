package com.makentoshe.habrachan.application.android.database.cache.record

import androidx.room.Entity

@Entity(primaryKeys = ["articleId", "flowId"])
data class ArticleFlowCrossRef(
    val articleId: Int, val flowId: Int
)