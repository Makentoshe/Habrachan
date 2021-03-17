package com.makentoshe.habrachan.application.android.database.record

import androidx.room.Entity

@Entity(primaryKeys = ["articleId", "flowId"])
data class ArticleFlowCrossRef(
    val articleId: Int, val flowId: Int
)