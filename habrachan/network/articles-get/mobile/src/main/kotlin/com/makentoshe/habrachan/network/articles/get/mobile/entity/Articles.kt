package com.makentoshe.habrachan.network.articles.get.mobile.entity

import com.makentoshe.habrachan.delegate.requireIntReadonlyProperty
import com.makentoshe.habrachan.delegate.requireReadonlyProperty
import com.makentoshe.habrachan.entity.article.Article
import com.makentoshe.habrachan.entity.article.ArticlePropertiesDelegate
import com.makentoshe.habrachan.functional.com.makentoshe.habrachan.AnyWithVolumeParameters
import com.makentoshe.habrachan.network.articles.get.entity.Articles
import com.makentoshe.habrachan.network.articles.get.entity.ArticlesPropertiesDelegate
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject

fun articlesProperties(pages: Int, articles: List<Article>) = mapOf(
    "pagesCount" to JsonPrimitive(pages),
    "articleRefs" to JsonObject(articles.associate { it.toString() to JsonObject(it.parameters) })
)

data class ArticlesPropertiesDelegateImpl(
    override val parameters: Map<String, JsonElement>,
    private val articlePropertiesDelegateFactory: (Map<String, JsonElement>) -> ArticlePropertiesDelegate,
) : ArticlesPropertiesDelegate, AnyWithVolumeParameters<JsonElement> {

    override val articles by requireReadonlyProperty("articleRefs") { jsonElement ->
        jsonElement.jsonObject.values.map(::articleFactory)
    }

    private fun articleFactory(jsonElement: JsonElement): Article {
        val parameters = jsonElement.jsonObject.toMap()
        return Article(parameters, articlePropertiesDelegateFactory(parameters))
    }

}

val Articles.pages by requireIntReadonlyProperty("pagesCount")
