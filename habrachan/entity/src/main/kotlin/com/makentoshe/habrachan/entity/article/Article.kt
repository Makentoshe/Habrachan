package com.makentoshe.habrachan.entity.article

import com.makentoshe.habrachan.Require
import com.makentoshe.habrachan.entity.article.author.ArticleAuthor
import com.makentoshe.habrachan.entity.article.component.ArticleId
import com.makentoshe.habrachan.entity.article.component.ArticleTitle
import com.makentoshe.habrachan.entity.article.hub.ArticleHub
import com.makentoshe.habrachan.entity.component.TimePublished
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
}

val Article.articleId get() = delegate.articleId

val Article.articleTitle get() = delegate.title

val Article.timePublished get() = delegate.timePublished

val Article.author get() = delegate.author

val Article.hubs get() = delegate.hubs
