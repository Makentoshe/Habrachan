package com.makentoshe.habrachan.application.android.database.record.article

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.makentoshe.habrachan.application.android.database.record.ArticleAuthorRecord
import com.makentoshe.habrachan.entity.Article

/**
 * Entity for storing articles in temporary table.
 * Articles will be fall here if they were loaded directly and will be removed
 * if the same article will be cached in the other table.
 */
@Entity
data class TempArticleRecord(
    @PrimaryKey
    override val articleId: Int,
    @Embedded
    override val author: ArticleAuthorRecord,
    override val commentsCount: Int,
    override val favoritesCount: Int,
    override val textHtml: String?,
    override val readingCount: Int,
    override val score: Int,
    override val timePublishedRaw: String,
    override val title: String,
    override val votesCount: Int
) : ArticleRecord() {

    constructor(article: Article):this(
        article.articleId,
        ArticleAuthorRecord(article.author),
        article.commentsCount,
        article.favoritesCount,
        article.textHtml,
        article.readingCount,
        article.score,
        article.timePublishedRaw,
        article.title,
        article.votesCount
    )
}
