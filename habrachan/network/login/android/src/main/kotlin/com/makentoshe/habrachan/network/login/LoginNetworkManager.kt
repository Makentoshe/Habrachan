package com.makentoshe.habrachan.network.login

import com.makentoshe.habrachan.api.AdditionalRequestParameters
import com.makentoshe.habrachan.api.android.AndroidHabrApi
import com.makentoshe.habrachan.api.android.login
import com.makentoshe.habrachan.api.android.login.*
import com.makentoshe.habrachan.api.android.login.entity.ClientId
import com.makentoshe.habrachan.api.android.login.entity.ClientSecret
import com.makentoshe.habrachan.api.android.login.entity.GrantType
import com.makentoshe.habrachan.api.login.LoginAuthBuilder
import com.makentoshe.habrachan.api.login.api.HabrLoginAuthApi
import com.makentoshe.habrachan.api.login.entity.Email
import com.makentoshe.habrachan.api.login.entity.Password
import com.makentoshe.habrachan.functional.Either
import com.makentoshe.habrachan.toRequire
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

class LoginNetworkManager(private val client: HttpClient) {

    private fun url(request: LoginRequest): HabrLoginAuthApi {
        return AndroidHabrApi.login().auth(request.loginAuth).build(request.parameters)
    }

    suspend fun execute(request: LoginRequest) : Either<LoginResponse, LoginException> = try {
        val map = Json.decodeFromString<JsonObject>(ktorResponse(request).readText()).toMap()
        Either.Left(LoginResponse(request, LoginSession(map)))
    } catch (exception: ClientRequestException) {
        val parameters = Json.decodeFromString<JsonObject>(exception.response.readText()).toMap()
        Either.Right(LoginException(request, exception, parameters))
    } catch (exception: Exception) {
        Either.Right(LoginException(request, exception, emptyMap()))
    }

    private fun ktorUrl(url: HabrLoginAuthApi): Url {
        return Url(url.path.plus("/"))
    }

    private suspend fun ktorRequest(request: LoginRequest): HttpResponse {
        return ktorRequest(url(request))
    }

    private suspend fun ktorRequest(api: HabrLoginAuthApi): HttpResponse {
        return client.post(ktorUrl(api)) {
            contentType(ContentType.Application.FormUrlEncoded)
            this.body = api.body
            api.headers.forEach { (key, value) -> header(key, value) }
        }
    }

    private suspend fun ktorResponse(request: LoginRequest): HttpResponse {
        return ktorRequest(request).call.response
    }
}

fun main() = runBlocking {
    val parameters = AdditionalRequestParameters(headers = mapOf(
        "client" to "85cab69095196f3.89453480",
        "apiKey" to "173984950848a2d27c0cc1c76ccf3d6d3dc8255b"
    ))
    val authBuilder = LoginAuthBuilder {
        this.email = Email("mkliverout@gmail.com").toRequire()
        this.password = Password("HabrMakentoshe1243568790").toRequire()
        this.clientSecret = ClientSecret("41ce71d623e04eab2cb8c00cf36bc14ec3aaf6d3").toRequire()
        this.clientId = ClientId("85cab69095196f3.89453480").toRequire()
        this.grantType = GrantType("password").toRequire()
    }
    // email=mkliverout%40gmail.com&password=HabrMakentoshe1243568790&client_secret=41ce71d623e04eab2cb8c00cf36bc14ec3aaf6d3&client_id=85cab69095196f3.89453480&grant_type=password
    // email=mkliverout%40gmail.com&password=HabrMakentoshe1243568790&client_secret=41ce71d623e04eab2cb8c00cf36bc14ec3aaf6d3&client_id=85cab69095196f3.89453480&grant_type=password

    val request = LoginRequest(parameters, authBuilder.build())
    val response = LoginNetworkManager(HttpClient{
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
    }).execute(request)

    println(response)
}
