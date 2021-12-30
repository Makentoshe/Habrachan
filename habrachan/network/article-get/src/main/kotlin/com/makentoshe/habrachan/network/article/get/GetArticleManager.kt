package com.makentoshe.habrachan.network.article.get

import com.makentoshe.habrachan.api.articles.api.HabrArticlesArticleApi
import com.makentoshe.habrachan.entity.article.Article
import com.makentoshe.habrachan.functional.Either2
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

abstract class GetArticleManager {

    protected abstract val client: HttpClient

    protected abstract fun api(request: GetArticleRequest): HabrArticlesArticleApi

    protected abstract fun article(json: String): Article

    suspend fun execute(request: GetArticleRequest): Either2<GetArticleResponse, GetArticleException> = try {
        val ktorResponse = ktorRequest(api(request)).call.response
        Either2.Left(GetArticleResponse(request, article(ktorResponse.readText())))
    } catch (exception: ClientRequestException) {
        val parameters = Json.decodeFromString<JsonObject>(exception.response.readText()).toMap()
        Either2.Right(GetArticleException(request, exception, parameters))
    } catch (exception: Exception) {
        Either2.Right(GetArticleException(request, exception, emptyMap()))
    }

    private suspend fun ktorRequest(api: HabrArticlesArticleApi): HttpResponse {
        return client.request(Url(api.path)) {
            api.headers.forEach { (key, value) -> header(key, value) }
            api.queries.forEach { (key, value) -> parameter(key, value) }
        }
    }
}