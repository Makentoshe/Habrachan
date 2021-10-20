package com.makentoshe.habrachan.entity.article

import com.makentoshe.habrachan.AnyWithVolumeParameters
import com.makentoshe.habrachan.entity.article.author.ArticleAuthor
import com.makentoshe.habrachan.entity.article.component.ArticleId
import com.makentoshe.habrachan.entity.article.component.ArticleText
import com.makentoshe.habrachan.entity.article.component.ArticleTitle
import com.makentoshe.habrachan.entity.article.hub.ArticleHub
import com.makentoshe.habrachan.entity.component.TimePublished
import com.makentoshe.habrachan.entity.component.timePublished
import com.makentoshe.habrachan.functional.Option2
import com.makentoshe.habrachan.functional.Require2
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
    /** Require2d to make requests and storing in datebase as a primary key*/
    val articleId: Require2<ArticleId>

    val title: Require2<ArticleTitle>

    val timePublished: Require2<TimePublished>

    val author: Require2<ArticleAuthor>

    /** In which hubs article was posted */
    val hubs: Require2<List<ArticleHub>>

    /** How many comments [Article] already have */
    val commentsCount: Require2<Int>

    /** How many users added this article to their bookmarks */
    val favoritesCount: Require2<Int>

    /** How many article was read. Each user can read article multiple times. */
    val readingCount: Require2<Int>

    /** The median of users likeability for article */
    val scoresCount: Require2<Int>

    /** How many users make a vote for article */
    val votesCount: Require2<Int>

    /** This field is option due to various apis. Part of them does not return an article text */
    val articleText: Option2<ArticleText>
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

    override val articleId: Require2<ArticleId>
        get() = Require2(ArticleId(articleId))

    override val articleText: Option2<ArticleText>
        get() = Option2.from(articleText?.let(::ArticleText))

    override val title: Require2<ArticleTitle>
        get() = Require2(ArticleTitle(articleTitle))

    override val author: Require2<ArticleAuthor>
        get() = Require2(articleAuthor)

    override val timePublished: Require2<TimePublished>
        get() = Require2(timePublished(timePublished))

    override val hubs: Require2<List<ArticleHub>>
        get() = Require2(articleHubs)

    override val commentsCount: Require2<Int>
        get() = Require2(commentsCount)

    override val favoritesCount: Require2<Int>
        get() = Require2(favoritesCount)

    override val readingCount: Require2<Int>
        get() = Require2(readingCount)

    override val votesCount: Require2<Int>
        get() = Require2(votesCount)

    override val scoresCount: Require2<Int>
        get() = Require2(scoresCount)
})