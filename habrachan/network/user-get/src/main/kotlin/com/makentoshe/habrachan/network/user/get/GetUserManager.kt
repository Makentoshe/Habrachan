package com.makentoshe.habrachan.network.user.get

import com.makentoshe.habrachan.api.HabrApiGet
import com.makentoshe.habrachan.api.mobile.MobileHabrApi
import com.makentoshe.habrachan.api.mobile.user
import com.makentoshe.habrachan.api.mobile.user.build
import com.makentoshe.habrachan.api.mobile.user.card
import com.makentoshe.habrachan.api.mobile.user.login
import com.makentoshe.habrachan.entity.user.get.NetworkUser
import com.makentoshe.habrachan.functional.Either2
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

class GetUserManager(private val client: HttpClient) {

    suspend fun execute(request: GetUserRequest) = try {
        val getUserKtorResponse = buildResponse(buildApi(request))
        val user = mapResponse(getUserKtorResponse.readText(), getUserKtorResponse.status.value)
        user.mapLeft { GetUserResponse(request, it) }
    } catch (exception: ClientRequestException) {
        getUserExceptionNetwork(request, exception)
    } catch (exception: Exception) {
        Either2.Right(GetUserException(request, exception))
    }

    private fun buildApi(request: GetUserRequest): HabrApiGet {
        return MobileHabrApi.user().login(request.login).card().build(request.parameters)
    }

    private suspend fun buildResponse(api: HabrApiGet): HttpResponse {
        return client.request(Url(api.path)) {
            api.headers.forEach { (key, value) -> header(key, value) }
            api.queries.forEach { (key, value) -> parameter(key, value) }
        }
    }

    private fun mapResponse(networkContent: String, networkStatusCode: Int): Either2<NetworkUser, GetUserException> {
        return Either2.Left(NetworkUser(Json.decodeFromString<JsonObject>(networkContent).toMap()))
    }

    private suspend fun getUserExceptionNetwork(
        request: GetUserRequest,
        exception: ClientRequestException,
    ): Either2.Right<GetUserException> {
        val parameters = getUserExceptionProperties(exception.response.status.value, exception.response.readText())
        return Either2.Right(GetUserException(request, exception, parameters))
    }
}
