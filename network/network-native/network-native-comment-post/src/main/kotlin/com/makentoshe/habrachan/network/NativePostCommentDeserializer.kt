package com.makentoshe.habrachan.network

import com.makentoshe.habrachan.functional.Result

class NativePostCommentDeserializer : NativeGsonDeserializer() {

    fun body(request: NativePostCommentRequest, json: String): Result<NativePostCommentResponse> = try {
        Result.success(gson.fromJson(json, NativePostCommentResponse.Factory::class.java).build(request))
    } catch (exception: Exception) {
        Result.failure(NativePostCommentException(request, json, exception.localizedMessage, exception))
    }

    fun error(request: NativePostCommentRequest, json: String): Result<NativePostCommentResponse> {
        return Result.failure(NativePostCommentException(request, json))
    }
}
