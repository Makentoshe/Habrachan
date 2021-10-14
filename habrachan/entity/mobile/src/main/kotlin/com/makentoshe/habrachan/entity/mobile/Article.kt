package com.makentoshe.habrachan.entity.mobile

import com.makentoshe.habrachan.delegate.*
import com.makentoshe.habrachan.entity.article.Article
import com.makentoshe.habrachan.entity.article.ArticlePropertiesDelegate
import com.makentoshe.habrachan.entity.article.author.ArticleAuthor
import com.makentoshe.habrachan.entity.article.component.articleId
import com.makentoshe.habrachan.entity.article.component.articleTitle
import com.makentoshe.habrachan.entity.article.flow.ArticleFlow
import com.makentoshe.habrachan.entity.article.hub.ArticleHub
import com.makentoshe.habrachan.entity.article.tag.ArticleTag
import com.makentoshe.habrachan.entity.component.timePublished
import com.makentoshe.habrachan.functional.com.makentoshe.habrachan.AnyWithVolumeParameters
import com.makentoshe.habrachan.functional.com.makentoshe.habrachan.delegate.optionStringReadonlyProperty
import kotlinx.serialization.json.*

fun article(
    id: Int,
    title: String,
    text: String?,
    timePublishedString: String,
    score: Int,
    commentsCount: Int,
    readingCount: Int,
    favoritesCount: Int,
    votesCount: Int,
    author: ArticleAuthor,
    hubs: List<ArticleHub>,
    flows: List<ArticleFlow>,
): Article {
    val properties = mapOf(
        "id" to JsonPrimitive(id),
        "titleHtml" to JsonPrimitive(title),
        "timePublished" to JsonPrimitive(timePublishedString),
        "author" to JsonObject(author.parameters),
        "textHtml" to JsonPrimitive(text),
        "statistics" to JsonObject(
            mapOf(
                "score" to JsonPrimitive(score),
                "commentsCount" to JsonPrimitive(commentsCount),
                "favoritesCount" to JsonPrimitive(favoritesCount),
                "readingCount" to JsonPrimitive(readingCount),
                "votesCount" to JsonPrimitive(votesCount)
            )
        ),
        "flows" to JsonArray(flows.map { JsonObject(it.parameters) }),
        "hubs" to JsonArray(hubs.map { JsonObject(it.parameters) })
    )
    return Article(properties, ArticlePropertiesDelegateImpl(properties))
}

data class ArticlePropertiesDelegateImpl(
    override val parameters: Map<String, JsonElement>
) : ArticlePropertiesDelegate, AnyWithVolumeParameters<JsonElement> {

    override val articleId by requireReadonlyProperty("id") { jsonElement ->
        articleId(jsonElement.jsonPrimitive.int)
    }

    override val title by requireReadonlyProperty("titleHtml") { jsonElement ->
        articleTitle(jsonElement.jsonPrimitive.content)
    }

    override val timePublished by requireReadonlyProperty("timePublished") { jsonElement ->
        timePublished(jsonElement.jsonPrimitive.content)
    }

    override val author by requireReadonlyProperty("author") { jsonElement ->
        ArticleAuthor(jsonElement.jsonObject.toMap())
    }
}

val Article.articleId by requireReadonlyProperty(
    "id", map = { jsonElement -> articleId(jsonElement.jsonPrimitive.int) }
)

val Article.articleTitle by requireReadonlyProperty(
    "titleHtml", map = { jsonElement -> articleTitle(jsonElement.jsonPrimitive.content) }
)

val Article.timePublished by requireReadonlyProperty(
    "timePublished", map = { jsonElement -> timePublished(jsonElement.jsonPrimitive.content) }
)

val Article.author by requireReadonlyProperty(
    "author", map = { jsonElement -> ArticleAuthor(jsonElement.jsonObject.toMap()) }
)

val Article.flows by requireListReadonlyProperty(
    "flows", mapElement = { flow -> ArticleFlow(flow.toMap()) }
)

val Article.hubs by requireListReadonlyProperty(
    "hubs", mapElement = { hub -> ArticleHub(hub.toMap()) }
)

val Article.tags by requireListReadonlyProperty(
    "tags", mapElement = { tag -> ArticleTag(tag.toMap()) }
)

val Article.isCorporative by requireBooleanReadonlyProperty("isCorporative")

val Article.lang by requireStringReadonlyProperty("lang")

val Article.editorVersion by requireStringReadonlyProperty("editorVersion")

val Article.postType by requireStringReadonlyProperty("postType")

val Article.commentsEnabled by requireBooleanReadonlyProperty("commentsEnabled")

val Article.votesEnabled by requireBooleanReadonlyProperty("votesEnabled")

val Article.isEditorial by requireBooleanReadonlyProperty("isEditorial")

val Article.textHtml by optionStringReadonlyProperty("textHtml")

val Article.commentsCount by requireIntReadonlyProperty("statistics", "commentsCount")

val Article.favoritesCount by requireIntReadonlyProperty("statistics", "favoritesCount")

val Article.readingCount by requireIntReadonlyProperty("statistics", "readingCount")

val Article.votesCount by requireIntReadonlyProperty("statistics", "votesCount")

val Article.scoreCount by requireIntReadonlyProperty("statistics", "score")
