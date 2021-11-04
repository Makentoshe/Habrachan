package com.makentoshe.habrachan.application.android.database.user.record

import androidx.room.Entity

@Entity(primaryKeys = ["title", "keyValuePair"])
data class ArticlesUserSearchArticlesFilterCrossRef(
    val title: String, val keyValuePair: String
)