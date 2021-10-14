package com.makentoshe.habrachan.entity.android

import com.makentoshe.habrachan.delegate.requireIntReadonlyProperty
import com.makentoshe.habrachan.delegate.requireListReadonlyProperty
import com.makentoshe.habrachan.delegate.requireReadonlyProperty
import com.makentoshe.habrachan.delegate.requireStringReadonlyProperty
import com.makentoshe.habrachan.entity.article.Article
import com.makentoshe.habrachan.entity.article.ArticlePropertiesDelegate
import com.makentoshe.habrachan.entity.article.author.ArticleAuthor
import com.makentoshe.habrachan.entity.article.author.ArticleAuthorPropertiesDelegate
import com.makentoshe.habrachan.entity.article.component.articleId
import com.makentoshe.habrachan.entity.article.component.articleTitle
import com.makentoshe.habrachan.entity.article.flow.ArticleFlow
import com.makentoshe.habrachan.entity.article.hub.ArticleHub
import com.makentoshe.habrachan.entity.component.timePublished
import com.makentoshe.habrachan.functional.com.makentoshe.habrachan.AnyWithVolumeParameters
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

data class ArticlePropertiesDelegateImpl(
    override val parameters: Map<String, JsonElement>,
    private val articleAuthorPropertiesDelegateFactory: (Map<String, JsonElement>) -> ArticleAuthorPropertiesDelegate,
) : ArticlePropertiesDelegate, AnyWithVolumeParameters<JsonElement> {

    override val articleId by requireReadonlyProperty("id") { jsonElement ->
        articleId(jsonElement.jsonPrimitive.int)
    }

    override val title by requireReadonlyProperty("title") { jsonElement ->
        articleTitle(jsonElement.jsonPrimitive.content)
    }

    override val timePublished by requireReadonlyProperty("time_published") { jsonElement ->
        timePublished(jsonElement.jsonPrimitive.content)
    }

    override val author by requireReadonlyProperty("author") { jsonElement ->
        val parameters = jsonElement.jsonObject.toMap()
        ArticleAuthor(parameters, articleAuthorPropertiesDelegateFactory(parameters))
    }
}

val Article.flows by requireListReadonlyProperty(
    "flows", mapElement = { flow -> ArticleFlow(flow.toMap()) }
)

val Article.hubs by requireListReadonlyProperty(
    "hubs", mapElement = { hub -> ArticleHub(hub.toMap()) }
)

val Article.tags by requireReadonlyProperty("tags_string") { jsonElement ->
    jsonElement.jsonPrimitive.content.split(", ").map(::articleTag)
}

val Article.isCorporative by requireIntReadonlyProperty("is_corporative")

val Article.lang by requireStringReadonlyProperty("lang")

val Article.editorVersion by requireIntReadonlyProperty("editor_version")

val Article.postType by requireStringReadonlyProperty("post_type_str")

val Article.textHtml by requireStringReadonlyProperty("text_html")

val Article.commentsCount by requireIntReadonlyProperty("comments_count")

val Article.favoritesCount by requireIntReadonlyProperty("favorites_count")

val Article.readingCount by requireIntReadonlyProperty("reading_count")

val Article.votesCount by requireIntReadonlyProperty("votes_count")

val Article.scoreCount by requireIntReadonlyProperty("score")
