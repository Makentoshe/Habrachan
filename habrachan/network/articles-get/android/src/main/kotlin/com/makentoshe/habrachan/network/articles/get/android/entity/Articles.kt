package com.makentoshe.habrachan.network.articles.get.android.entity

import com.makentoshe.habrachan.AnyWithVolumeParameters
import com.makentoshe.habrachan.delegate.optionReadonlyProperty
import com.makentoshe.habrachan.delegate.requireIntReadonlyProperty
import com.makentoshe.habrachan.delegate.requireListReadonlyProperty
import com.makentoshe.habrachan.delegate.requireStringReadonlyProperty
import com.makentoshe.habrachan.entity.article.Article
import com.makentoshe.habrachan.entity.article.ArticlePropertiesDelegate
import com.makentoshe.habrachan.network.articles.get.entity.Articles
import com.makentoshe.habrachan.network.articles.get.entity.ArticlesPropertiesDelegate
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

data class ArticlesPropertiesDelegateImpl(
    override val parameters: Map<String, JsonElement>,
    private val articlePropertiesDelegateFactory: (Map<String, JsonElement>) -> ArticlePropertiesDelegate,
) : ArticlesPropertiesDelegate, AnyWithVolumeParameters<JsonElement> {

    override val articles by requireListReadonlyProperty("data") { jsonObject ->
        jsonObject.toMap().let { Article(it, articlePropertiesDelegateFactory(it)) }
    }
}

val Articles.pages by requireIntReadonlyProperty("pages")

val Articles.sortedBy by requireStringReadonlyProperty("sorted_by")

val Articles.serverTime by requireStringReadonlyProperty("server_time")

val Articles.nextPage by optionReadonlyProperty("next_page") { jsonElement ->
    ArticlesPagination(jsonElement.jsonObject.toMap())
}
