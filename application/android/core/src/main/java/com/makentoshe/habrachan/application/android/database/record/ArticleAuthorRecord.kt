package com.makentoshe.habrachan.application.android.database.record

import androidx.room.*
import com.makentoshe.habrachan.entity.*

@Entity
data class ArticleAuthorRecord(
    @PrimaryKey
    override val userId: Int, override val login: String, override val fullname: String?, override val avatar: String
) : ArticleAuthor {
    constructor(articleAuthor: ArticleAuthor) : this(
        articleAuthor.userId, articleAuthor.login, articleAuthor.fullname, articleAuthor.avatar
    )

    fun toArticleAuthor() = articleAuthor(userId, avatar, login, fullname)
}

@Entity
data class FlowRecord2(
    val flowId: Int, val title: String, val alias: String
)

@Entity
data class HubRecord2(
    @PrimaryKey
    override val hubId: Int, override val title: String, override val alias: String
) : ArticleHub{
    constructor(articleHub: ArticleHub) : this(
        articleHub.hubId, articleHub.title, articleHub.alias
    )

    fun toArticleHub() = articleHub(hubId, title, alias)
}

@Entity(primaryKeys = ["articleId", "hubId"])
data class ArticleHubCrossRef(
    val articleId: Int, val hubId: Int
)

data class ArticleRecordWithHubRecords(
    @Embedded
    val article: ArticleRecord2,
    @Relation(
        parentColumn = "articleId",
        entityColumn = "hubId",
        associateBy = Junction(ArticleHubCrossRef::class)
    )
    val hubs: List<HubRecord2>
) {
    fun toArticle() = article.toArticle(hubs.map { it.toArticleHub() })
}

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
) {
    constructor(article: Article) : this(
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

    fun toArticle(hubs: List<ArticleHub>, flows: List<ArticleFlow> = emptyList()) = article(
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
