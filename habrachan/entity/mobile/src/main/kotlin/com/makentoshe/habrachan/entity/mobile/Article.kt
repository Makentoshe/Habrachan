package com.makentoshe.habrachan.entity.mobile

import com.makentoshe.habrachan.AnyWithVolumeParameters
import com.makentoshe.habrachan.delegate.*
import com.makentoshe.habrachan.entity.article.Article
import com.makentoshe.habrachan.entity.article.ArticlePropertiesDelegate
import com.makentoshe.habrachan.entity.article.author.ArticleAuthor
import com.makentoshe.habrachan.entity.article.author.ArticleAuthorPropertiesDelegate
import com.makentoshe.habrachan.entity.article.component.ArticleId
import com.makentoshe.habrachan.entity.article.component.ArticleText
import com.makentoshe.habrachan.entity.article.component.ArticleTitle
import com.makentoshe.habrachan.entity.article.flow.ArticleFlow
import com.makentoshe.habrachan.entity.article.hub.ArticleHub
import com.makentoshe.habrachan.entity.article.hub.ArticleHubPropertiesDelegate
import com.makentoshe.habrachan.entity.article.tag.ArticleTag
import com.makentoshe.habrachan.entity.component.timePublished
import kotlinx.serialization.json.*

fun articleProperties(
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
) = mapOf(
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

data class ArticlePropertiesDelegateImpl(
    override val parameters: Map<String, JsonElement>,
    private val articleAuthorPropertiesDelegateFactory: (Map<String, JsonElement>) -> ArticleAuthorPropertiesDelegate,
    private val articleHubPropertiesDelegateFactory: (Map<String, JsonElement>) -> ArticleHubPropertiesDelegate,
) : ArticlePropertiesDelegate, AnyWithVolumeParameters<JsonElement> {

    override val articleId by requireReadonlyProperty("id") { jsonElement ->
        ArticleId(jsonElement.jsonPrimitive.int)
    }

    override val title by requireReadonlyProperty("titleHtml") { jsonElement ->
        ArticleTitle(jsonElement.jsonPrimitive.content)
    }

    override val timePublished by requireReadonlyProperty("timePublished") { jsonElement ->
        timePublished(jsonElement.jsonPrimitive.content)
    }

    override val author by requireReadonlyProperty("author") { jsonElement ->
        val parameters = jsonElement.jsonObject.toMap()
        ArticleAuthor(parameters, articleAuthorPropertiesDelegateFactory(parameters))
    }

    override val hubs by requireListReadonlyProperty("hubs") { hub ->
        val parameters = hub.toMap()
        ArticleHub(parameters, articleHubPropertiesDelegateFactory(parameters))
    }

    override val articleText by optionReadonlyProperty("textHtml") { jsonElement ->
        ArticleText(jsonElement.jsonPrimitive.content)
    }

    override val commentsCount by requireReadonlyProperty(intPropertyMapper("statistics", "commentsCount"))

    override val favoritesCount by requireReadonlyProperty(intPropertyMapper("statistics", "favoritesCount"))

    override val readingCount by requireReadonlyProperty(intPropertyMapper("statistics", "readingCount"))

    override val votesCount by requireReadonlyProperty(intPropertyMapper("statistics", "votesCount"))

    override val scoresCount by requireReadonlyProperty(intPropertyMapper("statistics", "score"))
}

val Article.flows by requireListReadonlyProperty(
    "flows", mapElement = { flow -> ArticleFlow(flow.toMap()) }
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
