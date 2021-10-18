package com.makentoshe.habrachan.network.articles.get.mobile

import com.makentoshe.habrachan.api.AdditionalRequestParameters
import com.makentoshe.habrachan.api.articles.and
import com.makentoshe.habrachan.api.articles.api.HabrArticlesFilterApi
import com.makentoshe.habrachan.api.articles.filter.DailyArticlesPeriod
import com.makentoshe.habrachan.api.mobile.MobileHabrApi
import com.makentoshe.habrachan.api.mobile.articles
import com.makentoshe.habrachan.api.mobile.articles.*
import com.makentoshe.habrachan.entity.mobile.ArticleAuthorPropertiesDelegateImpl
import com.makentoshe.habrachan.entity.mobile.ArticleHubPropertiesDelegateImpl
import com.makentoshe.habrachan.entity.mobile.ArticlePropertiesDelegateImpl
import com.makentoshe.habrachan.functional.Either
import com.makentoshe.habrachan.network.articles.get.GetArticlesManager
import com.makentoshe.habrachan.network.articles.get.GetArticlesRequest
import com.makentoshe.habrachan.network.articles.get.entity.Articles
import com.makentoshe.habrachan.network.articles.get.mobile.entity.ArticlesPropertiesDelegateImpl
import io.ktor.client.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

class GetArticlesManagerImpl(client: HttpClient) : GetArticlesManager(client) {

    override fun url(request: GetArticlesRequest): HabrArticlesFilterApi {
        return MobileHabrApi.articles().filter(*request.filters).build(request.parameters)
    }

    override fun articles(json: String): Articles {
        val parameters = Json.decodeFromString<JsonObject>(json).toMap()
        return Articles(parameters, ArticlesPropertiesDelegateImpl(parameters) { articleParameters ->
            ArticlePropertiesDelegateImpl(articleParameters, ::ArticleAuthorPropertiesDelegateImpl, ::ArticleHubPropertiesDelegateImpl)
        })
    }
}

fun main() = runBlocking {
    val filters = sortArticlesFilter(DateArticlesSort) and pageArticlesFilter(1) and periodArticlesFilter(DailyArticlesPeriod)
    val request = GetArticlesRequest(AdditionalRequestParameters(), filters)
    val response = GetArticlesManagerImpl(HttpClient()).execute(request)
    println(response)

    println((response as Either.Left).value.request)

}