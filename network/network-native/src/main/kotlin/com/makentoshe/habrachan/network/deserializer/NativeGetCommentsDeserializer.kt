package com.makentoshe.habrachan.network.deserializer

import com.makentoshe.habrachan.network.exception.NativeGetArticleCommentsDeserializerException
import com.makentoshe.habrachan.network.request.NativeGetArticleCommentsRequest
import com.makentoshe.habrachan.network.response.NativeGetArticleCommentsResponse

class NativeGetCommentsDeserializer : NativeGsonDeserializer() {

    fun body(request: NativeGetArticleCommentsRequest, json: String): Result<NativeGetArticleCommentsResponse> = try {
        Result.success(gson.fromJson(json, NativeGetArticleCommentsResponse.Factory::class.java).build(request))
    } catch (exception: Exception) {
        Result.failure(exception)
    }

    fun error(request: NativeGetArticleCommentsRequest, json: String): Result<NativeGetArticleCommentsResponse> {
        return Result.failure(NativeGetArticleCommentsDeserializerException(request, json))
    }

}