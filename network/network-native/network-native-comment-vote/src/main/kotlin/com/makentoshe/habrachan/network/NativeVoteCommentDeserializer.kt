package com.makentoshe.habrachan.network

import com.makentoshe.habrachan.functional.Result

class NativeVoteCommentDeserializer : NativeGsonDeserializer() {

    fun success(request: NativeVoteCommentRequest, json: String, code: Int, message: String): Result<NativeVoteCommentResponse> = try {
        Result.success(gson.fromJson(json, NativeVoteCommentResponse.Factory::class.java).build(request))
    } catch (exception: Exception) {
        Result.failure(NativeVoteCommentException(request, json, listOf(message), code, message, exception))
    }

    fun failure(request: NativeVoteCommentRequest, json: String, code: Int, message: String): Result<NativeVoteCommentResponse> = try {
        Result.failure(gson.fromJson(json, NativeVoteCommentException.Factory::class.java).build(request, json))
    } catch (exception: Exception) {
        Result.failure(NativeVoteCommentException(request, json, listOf(message), code, message, exception))
    }
}