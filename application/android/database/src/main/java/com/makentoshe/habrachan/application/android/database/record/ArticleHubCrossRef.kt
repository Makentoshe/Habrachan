package com.makentoshe.habrachan.application.android.database.record

import androidx.room.Entity

@Entity(primaryKeys = ["articleId", "hubId"])
data class ArticleHubCrossRef(
    val articleId: Int, val hubId: Int
)

