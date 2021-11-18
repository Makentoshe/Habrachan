package com.makentoshe.habrachan.network.me.mobile

import com.makentoshe.habrachan.api.HabrApiGet
import com.makentoshe.habrachan.api.mobile.MobileHabrApi
import com.makentoshe.habrachan.api.mobile.user
import com.makentoshe.habrachan.api.mobile.user.build
import com.makentoshe.habrachan.api.mobile.user.me
import com.makentoshe.habrachan.api.user.HabrUserMeApi
import com.makentoshe.habrachan.entity.me.mobile.NetworkMeUser
import com.makentoshe.habrachan.functional.Either2
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

class MeUserManager(private val client: HttpClient) {

    suspend fun execute(request: MeUserRequest): Either2<MeUserResponse, MeUserException> = try {
        val meUserKtorResponse = internalRequest(internalMeUrl(request))

        val meUserKtorResponseText = meUserKtorResponse.readText()
        if (meUserKtorResponseText == "null") meUserExceptionNull(request, meUserKtorResponse) else {
            Either2.Left(MeUserResponse(request, meUser(meUserKtorResponseText)))
        }
    } catch (exception: ClientRequestException) {
        meUserExceptionNetwork(request, exception)
    } catch (exception: Exception) {
        Either2.Right(MeUserException(request, exception))
    }

    private suspend fun internalRequest(api: HabrApiGet): HttpResponse {
        return client.request(Url(api.path)) {
            api.headers.forEach { (key, value) -> header(key, value) }
            api.queries.forEach { (key, value) -> parameter(key, value) }
        }
    }

    private fun internalMeUrl(request: MeUserRequest): HabrUserMeApi {
        return MobileHabrApi.user().me().build(request.parameters)
    }

    private fun meUser(json: String): NetworkMeUser {
        return NetworkMeUser(Json.decodeFromString<JsonObject>(json).toMap())
    }

    private suspend fun meUserExceptionNull(
        request: MeUserRequest,
        response: HttpResponse,
    ): Either2.Right<MeUserException> {
        val parameters = meUserExceptionProperties(response.status.value, response.readText())
        return Either2.Right(MeUserException(request, parameters = parameters)) // "null"
    }

    private suspend fun meUserExceptionNetwork(
        request: MeUserRequest,
        exception: ClientRequestException,
    ): Either2.Right<MeUserException> {
        val parameters = meUserExceptionProperties(exception.response.status.value, exception.response.readText())
        return Either2.Right(MeUserException(request, exception, parameters))
    }
}
