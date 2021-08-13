package com.makentoshe.habrachan.network

import com.makentoshe.habrachan.functional.Result

class NativeGetArticleCommentsDeserializer : NativeGsonDeserializer() {

    fun body(request: NativeGetArticleCommentsRequest, json: String): Result<NativeGetArticleCommentsResponse> = try {
        Result.success(gson.fromJson(json, NativeGetArticleCommentsResponse.Factory::class.java).build(request))
    } catch (exception: Exception) {
        Result.failure(NativeGetArticleCommentsException(request, json, exception.localizedMessage, exception))
    }

    fun error(request: NativeGetArticleCommentsRequest, json: String): Result<NativeGetArticleCommentsResponse> {
        return Result.failure(NativeGetArticleCommentsException(request, json))
    }

}