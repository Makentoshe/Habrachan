package com.makentoshe.habrachan.network.login

import com.makentoshe.habrachan.api.mobile.MobileHabrApi
import com.makentoshe.habrachan.api.mobile.login
import com.makentoshe.habrachan.api.mobile.login.api.HabrLoginCookiesApi
import com.makentoshe.habrachan.api.mobile.login.build
import com.makentoshe.habrachan.api.mobile.login.cookies
import com.makentoshe.habrachan.functional.Either
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

class GetCookieNetworkManager(private val client: HttpClient) {

    suspend fun execute(request: GetCookieRequest): Either<GetCookieResponse, GetCookieException> = try {
        val api = MobileHabrApi.login().cookies().build(request.parameters)
        val ktorResponse = ktorCookiesRequest(api).call.response
        Either.Left(GetCookieResponse(request, ktorResponse.internalQueries, ktorResponse.internalHeaders))
    } catch (exception: ClientRequestException) {
        val parameters = Json.decodeFromString<JsonObject>(exception.response.readText()).toMap()
        Either.Right(GetCookieException(request, exception, parameters))
    } catch (exception: Exception) {
        Either.Right(GetCookieException(request, exception, emptyMap()))
    }

    private fun ktorCookiesUrl(url: HabrLoginCookiesApi): Url {
        return Url(url.path.plus("?").plus(url.queriesString))
    }

    private suspend fun ktorCookiesRequest(api: HabrLoginCookiesApi): HttpResponse {
        return client.request(ktorCookiesUrl(api)) {
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
