package com.makentoshe.habrachan.entity.android

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
import com.makentoshe.habrachan.entity.component.timePublished
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

data class ArticlePropertiesDelegateImpl(
    override val parameters: Map<String, JsonElement>,
    private val articleAuthorPropertiesDelegateFactory: (Map<String, JsonElement>) -> ArticleAuthorPropertiesDelegate,
    private val articleHubPropertiesDelegateFactory: (Map<String, JsonElement>) -> ArticleHubPropertiesDelegate,
) : ArticlePropertiesDelegate, AnyWithVolumeParameters<JsonElement> {

    override val articleId by requireReadonlyProperty("id") { jsonElement ->
        ArticleId(jsonElement.jsonPrimitive.int)
    }

    override val title by requireReadonlyProperty("title") { jsonElement ->
        ArticleTitle(jsonElement.jsonPrimitive.content)
    }

    override val timePublished by requireReadonlyProperty("time_published") { jsonElement ->
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

    override val articleText by optionReadonlyProperty("text_html") { jsonElement ->
        ArticleText(jsonElement.jsonPrimitive.content)
    }

    override val commentsCount by requireIntReadonlyProperty("comments_count")

    override val favoritesCount by requireIntReadonlyProperty("favorites_count")

    override val readingCount by requireIntReadonlyProperty("reading_count")

    override val votesCount by requireIntReadonlyProperty("votes_count")

    override val scoresCount by requireIntReadonlyProperty("score")
}

val Article.flows by requireListReadonlyProperty(
    "flows", mapElement = { flow -> ArticleFlow(flow.toMap()) }
)

val Article.tags by requireReadonlyProperty("tags_string") { jsonElement ->
    jsonElement.jsonPrimitive.content.split(", ").map(::articleTag)
}

val Article.isCorporative by requireIntReadonlyProperty("is_corporative")

val Article.lang by requireStringReadonlyProperty("lang")

val Article.editorVersion by requireIntReadonlyProperty("editor_version")

val Article.postType by requireStringReadonlyProperty("post_type_str")
