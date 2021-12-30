package com.makentoshe.habrachan.network.article.get.mobile

import com.makentoshe.habrachan.api.articles.api.HabrArticlesArticleApi
import com.makentoshe.habrachan.api.mobile.MobileHabrApi
import com.makentoshe.habrachan.api.mobile.articles
import com.makentoshe.habrachan.api.mobile.articles.article
import com.makentoshe.habrachan.api.mobile.articles.build
import com.makentoshe.habrachan.entity.article.Article
import com.makentoshe.habrachan.entity.article.hub.ArticleHubPropertiesDelegate
import com.makentoshe.habrachan.entity.mobile.ArticleAuthorPropertiesDelegateImpl
import com.makentoshe.habrachan.entity.mobile.ArticleHubPropertiesDelegateImpl
import com.makentoshe.habrachan.entity.mobile.ArticlePropertiesDelegateImpl
import com.makentoshe.habrachan.network.article.get.GetArticleManager
import com.makentoshe.habrachan.network.article.get.GetArticleRequest
import io.ktor.client.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

class GetArticleManagerImpl(override val client: HttpClient) : GetArticleManager() {

    override fun api(request: GetArticleRequest): HabrArticlesArticleApi {
        return MobileHabrApi.articles().article(request.articleId).build(request.parameters)
    }

    override fun article(json: String): Article {
        val parameters = Json.decodeFromString<JsonObject>(json).toMap()
        return Article(parameters, articlePropertiesDelegate(parameters))
    }

    private fun articlePropertiesDelegate(parameters: Map<String, JsonElement>) = ArticlePropertiesDelegateImpl(
        parameters, ::articleAuthorPropertiesDelegate, ::articleHubPropertiesDelegate,
    )

    private fun articleAuthorPropertiesDelegate(parameters: Map<String, JsonElement>): ArticleAuthorPropertiesDelegateImpl {
        return ArticleAuthorPropertiesDelegateImpl(parameters)
    }

    private fun articleHubPropertiesDelegate(parameters: Map<String, JsonElement>): ArticleHubPropertiesDelegate {
        return ArticleHubPropertiesDelegateImpl(parameters)
    }

}