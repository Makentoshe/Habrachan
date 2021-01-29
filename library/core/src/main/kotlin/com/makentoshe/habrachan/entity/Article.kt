package com.makentoshe.habrachan.entity

import java.util.*

interface Article : ArticleId {
    val title: String
    val timePublishedRaw: String
    val hubs: List<ArticleHub>
    val flows: List<ArticleFlow>
    val postType: Any // TODO string or int, so it is time to create a enum later
    val author: ArticleAuthor
    val score: Int
    val commentsCount: Int
    val readingCount: Int
    val favoritesCount: Int

    val timePublished: Date
}