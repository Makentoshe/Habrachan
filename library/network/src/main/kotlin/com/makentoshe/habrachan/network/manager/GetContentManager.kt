package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.fold
import com.makentoshe.habrachan.network.request.GetContentRequest
import com.makentoshe.habrachan.network.response.GetContentResponse
import okhttp3.OkHttpClient
import okhttp3.Request

class GetContentManager(private val client: OkHttpClient) {

    fun request(userSession: UserSession, url: String): GetContentRequest {
        return GetContentRequest(userSession, url)
    }

    fun content(request: GetContentRequest): Result<GetContentResponse> = try {
        client.newCall(Request.Builder().url(request.url).build()).execute().fold({
            Result.success(GetContentResponse(request, it.bytes()))
        }, {
            Result.failure(GetContentManagerException(request, it.string()))
        })
    } catch (exception: Exception) {
        Result.failure(GetContentManagerException(request, exception.localizedMessage))
    }

}

// TODO make special exception for whole manager exceptions
class GetContentManagerException(
    val request: GetContentRequest, override val message: String
) : Exception()