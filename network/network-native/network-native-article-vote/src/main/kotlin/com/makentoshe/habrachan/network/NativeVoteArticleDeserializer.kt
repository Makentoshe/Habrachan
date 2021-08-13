package com.makentoshe.habrachan.network

import com.makentoshe.habrachan.functional.Result

internal class NativeVoteArticleDeserializer : NativeGsonDeserializer() {

    fun success(request: NativeVoteArticleRequest, json: String, code: Int, message: String): Result<NativeVoteArticleResponse> = try {
        Result.success(gson.fromJson(json, NativeVoteArticleResponse.Factory::class.java).build(request))
    } catch (exception: Exception) {
        Result.failure(NativeVoteArticleException(request, json, listOf(message), code, message, exception))
    }

    fun failure(request: NativeVoteArticleRequest, json: String, code: Int, message: String): Result<NativeVoteArticleResponse> = try {
        Result.failure(gson.fromJson(json, NativeVoteArticleException.Factory::class.java).build(request, json))
    } catch (exception: Exception) {
        Result.failure(NativeVoteArticleException(request, json, listOf(message), code, message, exception))
    }
}