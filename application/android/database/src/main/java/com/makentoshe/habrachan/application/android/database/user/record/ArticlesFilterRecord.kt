package com.makentoshe.habrachan.application.android.database.user.record

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.makentoshe.habrachan.api.articles.ArticlesFilter
import com.makentoshe.habrachan.functional.Option2

@Entity
data class ArticlesFilterRecord(
    val key: String,

    val value: String,

    /** An article filter type. Might be ArticlesFilter, QueryArticlesFilter or PathArticlesFilter */
    val internalType: String,

    @PrimaryKey
    val keyValuePair: String = "$internalType(type=$internalType, $key=$value)"
)

fun ArticlesFilterRecord.toArticlesFilter(): Option2<ArticlesFilter> = Option2.from(
    when (internalType) {
        "PathArticlesFilter" -> ArticlesFilter.PathArticlesFilter(value)
        "QueryArticlesFilter" -> ArticlesFilter.QueryArticlesFilter(key, value)
        else -> null
    }
)