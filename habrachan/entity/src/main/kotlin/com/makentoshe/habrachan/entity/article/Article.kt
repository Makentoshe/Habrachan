package com.makentoshe.habrachan.entity.article

import com.makentoshe.habrachan.Require
import com.makentoshe.habrachan.entity.article.author.ArticleAuthor
import com.makentoshe.habrachan.entity.article.component.ArticleId
import com.makentoshe.habrachan.entity.article.component.ArticleTitle
import com.makentoshe.habrachan.entity.component.TimePublished
import com.makentoshe.habrachan.functional.com.makentoshe.habrachan.AnyWithVolumeParameters
import kotlinx.serialization.json.JsonElement

data class Article(
    override val parameters: Map<String, JsonElement>,
    val delegate: ArticlePropertiesDelegate
) : AnyWithVolumeParameters<JsonElement>

interface ArticlePropertiesDelegate {
    val articleId : Require<ArticleId>
    val title: Require<ArticleTitle>
    val timePublished : Require<TimePublished>
    val author: Require<ArticleAuthor>
}

val Article.articleId get() = delegate.articleId

val Article.articleTitle get() = delegate.title

val Article.timePublished get() = delegate.timePublished

val Article.author get() = delegate.author
