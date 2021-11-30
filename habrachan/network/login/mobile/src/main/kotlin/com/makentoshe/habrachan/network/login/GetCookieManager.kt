package com.makentoshe.habrachan.network.login

import com.makentoshe.habrachan.api.HabrApiGet
import com.makentoshe.habrachan.api.mobile.MobileHabrApi
import com.makentoshe.habrachan.api.mobile.login
import com.makentoshe.habrachan.api.mobile.login.build
import com.makentoshe.habrachan.api.mobile.login.cookies
import com.makentoshe.habrachan.functional.Either2
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

class GetCookieManager(private val client: HttpClient) {

    suspend fun execute(request: GetCookieRequest): Either2<GetCookieResponse, GetCookieException> = try {
        val ktorResponse = executeKtorCookies(request)
        Either2.Left(GetCookieResponse(request, ktorResponse.internalQueries, ktorResponse.internalHeaders))
    } catch (exception: ClientRequestException) {
        val parameters = Json.decodeFromString<JsonObject>(exception.response.readText()).toMap()
        Either2.Right(GetCookieException(request, exception, parameters))
    } catch (exception: Exception) {
        Either2.Right(GetCookieException(request, exception, emptyMap()))
    }

    private suspend fun executeKtorCookies(request: GetCookieRequest): HttpResponse {
        return ktorCookiesRequest(MobileHabrApi.login().cookies().build(request.parameters))
    }

    private suspend fun ktorCookiesRequest(api: HabrApiGet): HttpResponse {
        return client.request(api.path) {
            api.queries.forEach { (key, value) -> parameter(key, value) }
            api.headers.forEach { (key, value) -> header(key, value) }
        }
    }

    private val HttpResponse.internalQueries: Map<String, String>
        get() = request.url.parameters.flattenEntries().toMap()

    private val HttpResponse.internalHeaders: Map<String, String>
        get() = mapOf(
            "Referer" to request.url.toString(),
            "Set-Cookie" to headers.getAll("Set-Cookie")!!.joinToString(", ") { renderCookieHeader(parseServerSetCookieHeader(it)) }
        )
}
