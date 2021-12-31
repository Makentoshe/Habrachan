package com.makentoshe.habrachan.application.common.arena.article.get

import com.makentoshe.habrachan.AnyWithVolumeParameters
import com.makentoshe.habrachan.delegate.*
import com.makentoshe.habrachan.entity.article.component.ArticleId
import com.makentoshe.habrachan.entity.article.component.ArticleText
import com.makentoshe.habrachan.entity.article.component.ArticleTitle
import com.makentoshe.habrachan.entity.article.flow.ArticleFlow
import com.makentoshe.habrachan.entity.article.tag.ArticleTag
import com.makentoshe.habrachan.entity.component.timePublished
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

data class ArticleFromArena(
    override val parameters: Map<String, JsonElement>,
) : AnyWithVolumeParameters<JsonElement>

val ArticleFromArena.articleId by requireReadonlyProperty("id") { jsonElement ->
    ArticleId(jsonElement.jsonPrimitive.int)
}

val ArticleFromArena.title by requireReadonlyProperty("titleHtml") { jsonElement ->
    ArticleTitle(jsonElement.jsonPrimitive.content)
}

val ArticleFromArena.timePublished by requireReadonlyProperty("timePublished") { jsonElement ->
    timePublished(jsonElement.jsonPrimitive.content)
}

val ArticleFromArena.author by requireReadonlyProperty("author") { jsonElement ->
    ArticleAuthorFromArena(jsonElement.jsonObject.toMap())
}

val ArticleFromArena.hubs by requireListReadonlyProperty("hubs") { jsonElement ->
    ArticleHubFromArena(jsonElement.jsonObject.toMap())
}

val ArticleFromArena.articleText by optionReadonlyProperty("textHtml") { jsonElement ->
    ArticleText(jsonElement.jsonPrimitive.content)
}

val ArticleFromArena.commentsCount by requireReadonlyProperty(intPropertyMapper("statistics", "commentsCount"))

val ArticleFromArena.favoritesCount by requireReadonlyProperty(intPropertyMapper("statistics", "favoritesCount"))

val ArticleFromArena.readingCount by requireReadonlyProperty(intPropertyMapper("statistics", "readingCount"))

val ArticleFromArena.votesCount by requireReadonlyProperty(intPropertyMapper("statistics", "votesCount"))

val ArticleFromArena.scoresCount by requireReadonlyProperty(intPropertyMapper("statistics", "score"))

val ArticleFromArena.flows by requireListReadonlyProperty(
    "flows", mapElement = { flow -> ArticleFlow(flow.toMap()) }
)

val ArticleFromArena.tags by requireListReadonlyProperty(
    "tags", mapElement = { tag -> ArticleTag(tag.toMap()) }
)

val ArticleFromArena.isCorporative by requireBooleanReadonlyProperty("isCorporative")

val ArticleFromArena.lang by requireStringReadonlyProperty("lang")

val ArticleFromArena.editorVersion by requireStringReadonlyProperty("editorVersion")

val ArticleFromArena.postType by requireStringReadonlyProperty("postType")

val ArticleFromArena.commentsEnabled by requireBooleanReadonlyProperty("commentsEnabled")

val ArticleFromArena.votesEnabled by requireBooleanReadonlyProperty("votesEnabled")

val ArticleFromArena.isEditorial by requireBooleanReadonlyProperty("isEditorial")
