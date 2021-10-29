package com.makentoshe.habrachan.network.login

import com.makentoshe.habrachan.api.android.AndroidHabrApi
import com.makentoshe.habrachan.api.android.login
import com.makentoshe.habrachan.api.android.login.auth
import com.makentoshe.habrachan.api.android.login.build
import com.makentoshe.habrachan.api.login.api.HabrLoginAuthApi
import com.makentoshe.habrachan.functional.Either2
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

class GetLoginManager(private val client: HttpClient) {

    private fun url(request: GetLoginRequest): HabrLoginAuthApi {
        return AndroidHabrApi.login().auth(request.loginAuth).build(request.parameters)
    }

    suspend fun execute(request: GetLoginRequest) : Either2<LoginResponse, LoginException> = try {
        val map = Json.decodeFromString<JsonObject>(ktorResponse(request).readText()).toMap()
        Either2.Left(LoginResponse(request, LoginSession(map)))
    } catch (exception: ClientRequestException) {
        val parameters = Json.decodeFromString<JsonObject>(exception.response.readText()).toMap()
        Either2.Right(LoginException(request, exception, parameters))
    } catch (exception: Exception) {
        Either2.Right(LoginException(request, exception, emptyMap()))
    }

    private fun ktorUrl(url: HabrLoginAuthApi): Url {
        return Url(url.path.plus("/"))
    }

    private suspend fun ktorRequest(request: GetLoginRequest): HttpResponse {
        return ktorRequest(url(request))
    }

    private suspend fun ktorRequest(api: HabrLoginAuthApi): HttpResponse {
        return client.post(ktorUrl(api)) {
            contentType(ContentType.Application.FormUrlEncoded)
            this.body = api.body
            api.headers.forEach { (key, value) -> header(key, value) }
        }
    }

    private suspend fun ktorResponse(request: GetLoginRequest): HttpResponse {
        return ktorRequest(request).call.response
    }
}
