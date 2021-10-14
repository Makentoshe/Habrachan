package com.makentoshe.habrachan.network.articles.get

import com.makentoshe.habrachan.api.articles.api.HabrArticlesFilterApi
import com.makentoshe.habrachan.functional.Either
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

    suspend fun execute(request: GetArticlesRequest): Either<GetArticlesResponse, GetArticlesException> = try {
        val articles = Articles(Json.decodeFromString<JsonObject>(ktorResponse(request).readText()).toMap())
        Either.Left(GetArticlesResponse(request, articles))
    } catch (exception: ClientRequestException) {
        val parameters = Json.decodeFromString<JsonObject>(exception.response.readText()).toMap()
        Either.Right(GetArticlesException(request, exception, parameters))
    } catch (exception: Exception) {
        Either.Right(GetArticlesException(request, exception, emptyMap()))
    }

    private fun ktorUrl(url: HabrArticlesFilterApi): Url {
        return Url(url.path.plus("?").plus(url.queriesString))
    }

    private suspend fun ktorRequest(request: GetArticlesRequest): HttpResponse {
        return ktorRequest(url(request))
    }

    private suspend fun ktorRequest(api: HabrArticlesFilterApi): HttpResponse {
        return client.request(ktorUrl(api)) {
            api.headers.forEach { (key, value) -> header(key, value) }
        }
    }

    private suspend fun ktorResponse(request: GetArticlesRequest): HttpResponse {
        return ktorRequest(request).call.response
    }

}