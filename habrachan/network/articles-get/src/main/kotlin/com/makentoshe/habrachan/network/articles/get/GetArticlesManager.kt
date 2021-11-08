package com.makentoshe.habrachan.network.articles.get

import com.makentoshe.habrachan.api.articles.api.HabrArticlesFilterApi
import com.makentoshe.habrachan.functional.Either2
import com.makentoshe.habrachan.network.articles.get.entity.Articles
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

abstract class GetArticlesManager(private val client: HttpClient) {

    protected abstract fun url(request: GetArticlesRequest): HabrArticlesFilterApi

    protected abstract fun articles(json: String): Articles

    suspend fun execute(request: GetArticlesRequest): Either2<GetArticlesResponse, GetArticlesException> = try {
        Either2.Left(GetArticlesResponse(request, articles(ktorResponse(request).readText())))
    } catch (exception: ClientRequestException) {
        val parameters = Json.decodeFromString<JsonObject>(exception.response.readText()).toMap()
        Either2.Right(GetArticlesException(request, exception, parameters))
    } catch (exception: Exception) {
        Either2.Right(GetArticlesException(request, exception, emptyMap()))
    }

    private fun ktorUrl(url: HabrArticlesFilterApi): Url {
//        return Url(url.path.plus("?").plus(url.queriesString))
        return Url(url.path)
    }

    private suspend fun ktorRequest(request: GetArticlesRequest): HttpResponse {
        return ktorRequest(url(request))
    }

    private suspend fun ktorRequest(api: HabrArticlesFilterApi): HttpResponse {
        return client.request(ktorUrl(api)) {
            api.headers.forEach { (key, value) -> header(key, value) }
            api.queries.forEach { (key, value) -> parameter(key, value) }
        }
    }

    private suspend fun ktorResponse(request: GetArticlesRequest): HttpResponse {
        return ktorRequest(request).call.response
    }

}