package com.makentoshe.habrachan.network

import com.makentoshe.habrachan.functional.Result

internal class NativeGetArticleDeserializer : NativeGsonDeserializer() {

    fun body(request: NativeGetArticleRequest, json: String): Result<NativeGetArticleResponse.Factory> = try {
        Result.success(gson.fromJson(json, NativeGetArticleResponse.Factory::class.java))
    } catch (exception: Exception) {
        Result.failure(NativeGetArticleException(request, json, cause = exception))
    }

    fun error(request: NativeGetArticleRequest, json: String): Result<NativeGetArticleResponse.Factory> {
        return Result.failure(NativeGetArticleException(request, json))
    }
}