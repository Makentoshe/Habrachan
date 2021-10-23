package com.makentoshe.habrachan.network.articles.get.mobile.entity

import com.makentoshe.habrachan.AnyWithVolumeParameters
import com.makentoshe.habrachan.delegate.optionIntReadonlyProperty
import com.makentoshe.habrachan.delegate.propertyMapper
import com.makentoshe.habrachan.delegate.requireReadonlyProperty
import com.makentoshe.habrachan.entity.article.Article
import com.makentoshe.habrachan.entity.article.ArticlePropertiesDelegate
import com.makentoshe.habrachan.network.articles.get.entity.ArticlesPropertiesDelegate
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject

fun articlesProperties(articles: List<Article>, pages: Int? = null) = mapOf(
    "pagesCount" to JsonPrimitive(pages),
    "articleRefs" to JsonObject(articles.associate { it.toString() to JsonObject(it.parameters) })
)

data class ArticlesPropertiesDelegateImpl(
    override val parameters: Map<String, JsonElement>,
    private val articlePropertiesDelegateFactory: (Map<String, JsonElement>) -> ArticlePropertiesDelegate,
) : ArticlesPropertiesDelegate, AnyWithVolumeParameters<JsonElement> {

    override val articles by requireReadonlyProperty(
        propertyMapper("articleRefs", map = ::articlesFactory),
        propertyMapper("articles", "articleRefs", map = ::articlesFactory)
    )

    override val totalPages by optionIntReadonlyProperty("pagesCount")

    private fun articleFactory(jsonElement: JsonElement): Article {
        val parameters = jsonElement.jsonObject.toMap()
        return Article(parameters, articlePropertiesDelegateFactory(parameters))
    }

    private fun articlesFactory(jsonElement: JsonElement): List<Article> {
        return jsonElement.jsonObject.values.map(::articleFactory)
    }

}
