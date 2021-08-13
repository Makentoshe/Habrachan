package com.makentoshe.habrachan.network

import com.makentoshe.habrachan.functional.Result

internal class NativeGetUserDeserializer : NativeGsonDeserializer() {

    fun success(request: NativeGetUserRequest, json: String): Result<NativeGetUserResponse> = try {
        Result.success(gson.fromJson(json, NativeGetUserResponse.Factory::class.java).build(request))
    } catch (exception: Exception) {
        Result.failure(NativeGetUserException(request, raw = json, message = exception.localizedMessage, cause = exception))
    }

    fun failure(request: NativeGetUserRequest, json: String): Result<NativeGetUserResponse> = try {
        Result.failure(gson.fromJson(json, NativeGetUserException.Factory::class.java).build(request, json))
    } catch (exception: Exception) {
        Result.failure(NativeGetUserException(request, raw = json, message = exception.localizedMessage, cause = exception))
    }
}