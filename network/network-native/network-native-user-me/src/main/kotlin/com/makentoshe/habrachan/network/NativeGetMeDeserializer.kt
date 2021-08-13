package com.makentoshe.habrachan.network

import com.makentoshe.habrachan.functional.Result

internal class NativeGetMeDeserializer : NativeGsonDeserializer() {

    fun body(request: NativeGetMeRequest, json: String): Result<NativeGetMeResponse> = try {
        Result.success(gson.fromJson(json, NativeGetMeResponse.Factory::class.java).build(request))
    } catch (exception: Exception) {
        Result.failure(NativeGetMeException(request, json, message = exception.localizedMessage, cause = exception))
    }

    // TODO(medium) Add proper deserialization
    fun error(request: NativeGetMeRequest, json: String): Result<NativeGetMeResponse> {
        return Result.failure(NativeGetMeException(request, json))
    }
}