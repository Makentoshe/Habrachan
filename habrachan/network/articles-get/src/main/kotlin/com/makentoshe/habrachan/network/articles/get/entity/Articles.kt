package com.makentoshe.habrachan.network.articles.get.entity

import com.makentoshe.habrachan.Require
import com.makentoshe.habrachan.entity.article.Article
import com.makentoshe.habrachan.functional.com.makentoshe.habrachan.AnyWithVolumeParameters
import kotlinx.serialization.json.JsonElement

data class Articles(
    override val parameters: Map<String, JsonElement>,
    val delegate: ArticlesPropertiesDelegate,
) : AnyWithVolumeParameters<JsonElement>

interface ArticlesPropertiesDelegate {
    val articles: Require<List<Article>>
}

val Articles.articles: Require<List<Article>> get() = delegate.articles

/**
 * This factory method allows to declare a custom [Articles] instance only with delegate.
 *
 * In this case any parameters delegates will not work correctly.
 * */
fun articles(list: List<Article>) = Articles(emptyMap(), object : ArticlesPropertiesDelegate {
    override val articles = Require(list)
})
