package com.makentoshe.habrachan.application.android.database.cache.record

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.makentoshe.habrachan.entity.article.Article
import com.makentoshe.habrachan.entity.article.article
import com.makentoshe.habrachan.entity.article.author.ArticleAuthor
import com.makentoshe.habrachan.entity.article.author.articleAuthor
import com.makentoshe.habrachan.entity.article.hub.ArticleHub
import com.makentoshe.habrachan.entity.article.hub.articleHub
import kotlinx.serialization.json.JsonElement

@Entity(primaryKeys = ["articleAuthorId", "articleId"])
data class ArticleAuthorCrossRef(
    val articleId: Int,
    val articleAuthorId: Int
)

@Entity
data class ArticleAuthorRecord3(
    @PrimaryKey
    val authorId: Int,
    val authorLogin: String,
    val authorAvatar: String?
) {

    constructor(articleAuthor: ArticleAuthor) : this(
        authorId = articleAuthor.delegate.authorId.value.authorId,
        authorLogin = articleAuthor.delegate.authorLogin.value.authorLogin,
        authorAvatar = articleAuthor.delegate.authorAvatar.getOrNull()?.avatarUrl
    )

    fun toArticleAuthor(parameters: Map<String, JsonElement> = emptyMap()) = articleAuthor(
        authorId, authorLogin, authorAvatar, parameters
    )
}

@Entity
data class ArticleHubRecord3(
    @PrimaryKey
    val hubId: Int,
    val title: String,
) {

    constructor(articleHub: ArticleHub) : this(
        hubId = articleHub.delegate.hubId.value.hubId,
        title = articleHub.delegate.title.value
    )

    fun toArticleHub(parameters: Map<String, JsonElement> = emptyMap()) = articleHub(hubId, title, parameters)
}

@Entity
data class ArticleRecord3(
    @PrimaryKey
    val articleId: Int,
    val articleTitle: String,
    val articleText: String?,
    val scoresCount: Int,
    val votesCount: Int,
    val favoritesCount: Int,
    val commentsCount: Int,
    val readingCount: Int,
    val timePublished: String,
    val internalType: String,
) {

    constructor(type: String, article: Article) : this(
        internalType = type,
        articleId = article.delegate.articleId.value.articleId,
        articleTitle = article.delegate.title.value.articleTitle,
        articleText = article.delegate.articleText.getOrNull()?.html,
        scoresCount = article.delegate.scoresCount.value,
        votesCount = article.delegate.votesCount.value,
        favoritesCount = article.delegate.favoritesCount.value,
        commentsCount = article.delegate.commentsCount.value,
        readingCount = article.delegate.readingCount.value,
        timePublished = article.delegate.timePublished.value.timePublishedString
    )

    fun toArticle(
        articleAuthor: ArticleAuthor,
        articleHubs: List<ArticleHub>,
        parameters: Map<String, JsonElement> = emptyMap()
    ) = article(
        articleId,
        articleTitle,
        articleAuthor,
        timePublished,
        articleHubs,
        articleText,
        commentsCount,
        favoritesCount,
        readingCount,
        votesCount,
        scoresCount,
        parameters,
    )

}
