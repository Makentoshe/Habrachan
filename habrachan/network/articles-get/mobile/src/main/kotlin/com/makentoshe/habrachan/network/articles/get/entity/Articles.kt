package com.makentoshe.habrachan.network.articles.get.entity

import com.makentoshe.habrachan.delegate.requireIntReadonlyProperty
import com.makentoshe.habrachan.delegate.requireReadonlyProperty
import com.makentoshe.habrachan.entity.article.Article
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject

fun articles(pages: Int, articles: List<Article>) = Articles(mapOf(
    "pagesCount" to JsonPrimitive(pages),
    "articleRefs" to JsonObject(articles.associate { it.toString() to JsonObject(it.parameters) })
))

val Articles.pages by requireIntReadonlyProperty("pagesCount")

val Articles.articles by requireReadonlyProperty("articleRefs") { jsonElement ->
    jsonElement.jsonObject.values.map { jsonElement -> Article(jsonElement.jsonObject.toMap()) }
}