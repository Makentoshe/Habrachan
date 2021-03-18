package com.makentoshe.habrachan.application.android.database.record

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.makentoshe.habrachan.entity.Article
import com.makentoshe.habrachan.entity.ArticleFlow
import com.makentoshe.habrachan.entity.ArticleHub
import com.makentoshe.habrachan.entity.article

@Entity
data class ArticleRecord2(
    @PrimaryKey
    val articleId: Int,
    @Embedded
    val author: ArticleAuthorRecord,
    val commentsCount: Int,
    val favoritesCount: Int,
    val textHtml: String? = "",
    val readingCount: Int,
    val score: Int,
    val timePublishedRaw: String,
    val title: String,
    val votesCount: Int = 0,
    /** One of the SpecTypes: "all", "top", "interesting", "subscription" or "" (empty) */
    val type: String
) {
    constructor(type: String, article: Article) : this(
        article.articleId,
        ArticleAuthorRecord(article.author),
        article.commentsCount,
        article.favoritesCount,
        article.textHtml,
        article.readingCount,
        article.score,
        article.timePublishedRaw,
        article.title,
        article.votesCount,
        type
    )

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

    companion object Types {
        val ALL = "all"
        val INTERESTING = "interesting"
        val TOP = "top"
        val SUBSCRIBE = "subscribe"
        val UNDEFINED = ""
    }
}