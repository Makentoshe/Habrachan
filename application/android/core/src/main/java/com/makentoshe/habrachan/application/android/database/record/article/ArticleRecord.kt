package com.makentoshe.habrachan.application.android.database.record.article

import com.makentoshe.habrachan.application.android.database.record.ArticleAuthorRecord
import com.makentoshe.habrachan.entity.ArticleFlow
import com.makentoshe.habrachan.entity.ArticleHub
import com.makentoshe.habrachan.entity.article

// TODO add order: Int value for containing correct value position in search
abstract class ArticleRecord {
    abstract val articleId: Int
    abstract val author: ArticleAuthorRecord
    abstract val commentsCount: Int
    abstract val favoritesCount: Int
    abstract val textHtml: String?
    abstract val readingCount: Int
    abstract val score: Int
    abstract val timePublishedRaw: String
    abstract val title: String
    abstract val votesCount: Int

    fun toArticle(hubs: List<ArticleHub>, flows: List<ArticleFlow>) = article(
        articleId,
        title,
        timePublishedRaw,
        score,
        commentsCount,
        readingCount,
        favoritesCount,
        votesCount,
        author.toArticleAuthor(),
        hubs,
        flows,
        textHtml
    )
}
