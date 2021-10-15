package com.makentoshe.habrachan.network.articles.get

import com.makentoshe.habrachan.api.android.AndroidHabrApi
import com.makentoshe.habrachan.api.android.articles
import com.makentoshe.habrachan.api.android.articles.build
import com.makentoshe.habrachan.api.android.articles.filter
import com.makentoshe.habrachan.api.articles.api.HabrArticlesFilterApi
import com.makentoshe.habrachan.entity.android.ArticleAuthorPropertiesDelegateImpl
import com.makentoshe.habrachan.entity.android.ArticleHubPropertiesDelegateImpl
import com.makentoshe.habrachan.entity.android.ArticlePropertiesDelegateImpl
import com.makentoshe.habrachan.network.articles.get.entity.Articles
import com.makentoshe.habrachan.network.articles.get.entity.ArticlesPropertiesDelegateImpl
import io.ktor.client.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

class GetArticlesManagerImpl(client: HttpClient) : GetArticlesManager(client) {

    override fun url(request: GetArticlesRequest): HabrArticlesFilterApi {
        return AndroidHabrApi.articles().filter(*request.filters).build(request.parameters)
    }

    override fun articles(json: String): Articles {
        val parameters = Json.decodeFromString<JsonObject>(json).toMap()
        return Articles(parameters, ArticlesPropertiesDelegateImpl(parameters) { articleParameters ->
            ArticlePropertiesDelegateImpl(articleParameters, ::ArticleAuthorPropertiesDelegateImpl, ::ArticleHubPropertiesDelegateImpl)
        })
    }

}