package com.makentoshe.habrachan.network

import com.makentoshe.habrachan.functional.Result
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
            Result.failure(GetContentException(request, message = it.string()))
        })
    } catch (exception: Exception) {
        Result.failure(GetContentException(request, message = exception.localizedMessage, cause = exception))
    }

}
