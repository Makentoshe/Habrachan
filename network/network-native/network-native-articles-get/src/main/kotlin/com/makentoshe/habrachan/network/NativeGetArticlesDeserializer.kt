package com.makentoshe.habrachan.network

import com.makentoshe.habrachan.functional.Result

internal class NativeGetArticlesDeserializer : NativeGsonDeserializer() {

    fun body(request: NativeGetArticlesRequest, json: String): Result<NativeGetArticlesResponse> = try {
        Result.success(gson.fromJson(json, NativeGetArticlesResponse.Factory::class.java).build(request))
    } catch (exception: Exception) {
        Result.failure(NativeGetArticlesException(request, json, message = exception.message, cause = exception))
    }

    fun error(request: NativeGetArticlesRequest, json: String): Result<NativeGetArticlesResponse> {
        return Result.failure(NativeGetArticlesException(request, json))
    }
}
