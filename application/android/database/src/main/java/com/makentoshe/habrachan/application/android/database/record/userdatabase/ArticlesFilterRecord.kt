package com.makentoshe.habrachan.application.android.database.record.userdatabase

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.makentoshe.habrachan.Option
import com.makentoshe.habrachan.api.articles.ArticlesFilter

@Entity
data class ArticlesFilterRecord(
    val key: String,

    val value: String,

    /** An article filter type. Might be [ArticlesFilter], [QueryArticlesFilter] or [PathArticlesFilter]*/
    val internalType: String,

    @PrimaryKey
    val keyValuePair: String = "$internalType(type=$internalType, $key=$value)"
)

fun ArticlesFilterRecord.toArticlesFilter(): Option<ArticlesFilter> = Option.from(
    when (internalType) {
        "PathArticlesFilter" -> ArticlesFilter.PathArticlesFilter(key, value)
        "QueryArticlesFilter" -> ArticlesFilter.QueryArticlesFilter(key, value)
        else -> null
    }
)