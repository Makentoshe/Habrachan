package com.makentoshe.habrachan.entity.article

import com.makentoshe.habrachan.Option
import com.makentoshe.habrachan.Require
import com.makentoshe.habrachan.entity.article.author.ArticleAuthor
import com.makentoshe.habrachan.entity.article.component.ArticleId
import com.makentoshe.habrachan.entity.article.component.ArticleText
import com.makentoshe.habrachan.entity.article.component.ArticleTitle
import com.makentoshe.habrachan.entity.article.hub.ArticleHub
import com.makentoshe.habrachan.entity.component.TimePublished
import com.makentoshe.habrachan.entity.component.timePublished
import com.makentoshe.habrachan.functional.com.makentoshe.habrachan.AnyWithVolumeParameters
import kotlinx.serialization.json.JsonElement

data class Article(
    override val parameters: Map<String, JsonElement>,
    val delegate: ArticlePropertiesDelegate
) : AnyWithVolumeParameters<JsonElement>

/**
 * This is a special delegated interface that declares which field(parameters) are
 * mostly important in any instance of [Article] and should be in one call access.
 * */
interface ArticlePropertiesDelegate {
    /** Required to make requests and storing in datebase as a primary key*/
    val articleId: Require<ArticleId>

    val title: Require<ArticleTitle>

    val timePublished: Require<TimePublished>

    val author: Require<ArticleAuthor>

    /** In which hubs article was posted */
    val hubs: Require<List<ArticleHub>>

    /** How many comments [Article] already have */
    val commentsCount: Require<Int>

    /** How many users added this article to their bookmarks */
    val favoritesCount: Require<Int>

    /** How many article was read. Each user can read article multiple times. */
    val readingCount: Require<Int>

    /** The median of users likeability for article */
    val scoresCount: Require<Int>

    /** How many users make a vote for article */
    val votesCount: Require<Int>

    /** This field is option due to various apis. Part of them does not return an article text */
    val articleText: Option<ArticleText>
}

val Article.articleId get() = delegate.articleId

val Article.articleTitle get() = delegate.title

val Article.articleText get() = delegate.articleText

val Article.timePublished get() = delegate.timePublished

val Article.author get() = delegate.author

val Article.hubs get() = delegate.hubs

val Article.commentsCount get() = delegate.commentsCount

val Article.favoritesCount get() = delegate.favoritesCount

val Article.readingCount get() = delegate.readingCount

val Article.votesCount get() = delegate.votesCount

val Article.scoresCount get() = delegate.scoresCount


fun article(
    articleId: Int,
    articleTitle: String,
    articleAuthor: ArticleAuthor,
    timePublished: String,
    articleHubs: List<ArticleHub>,
    articleText: String?,
    commentsCount: Int,
    favoritesCount: Int,
    readingCount: Int,
    votesCount: Int,
    scoresCount: Int,
    articleParameters: Map<String, JsonElement> = emptyMap()
) = Article(articleParameters, object : ArticlePropertiesDelegate {

    override val articleId: Require<ArticleId>
        get() = Require(ArticleId(articleId))

    override val articleText: Option<ArticleText>
        get() = Option.from(articleText?.let(::ArticleText))

    override val title: Require<ArticleTitle>
        get() = Require(ArticleTitle(articleTitle))

    override val author: Require<ArticleAuthor>
        get() = Require(articleAuthor)

    override val timePublished: Require<TimePublished>
        get() = Require(timePublished(timePublished))

    override val hubs: Require<List<ArticleHub>>
        get() = Require(articleHubs)

    override val commentsCount: Require<Int>
        get() = Require(commentsCount)

    override val favoritesCount: Require<Int>
        get() = Require(favoritesCount)

    override val readingCount: Require<Int>
        get() = Require(readingCount)

    override val votesCount: Require<Int>
        get() = Require(votesCount)

    override val scoresCount: Require<Int>
        get() = Require(scoresCount)
})