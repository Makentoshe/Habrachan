package com.makentoshe.habrachan.network.deserializer

import com.makentoshe.habrachan.network.exception.NativeGetUserDeserializerException
import com.makentoshe.habrachan.network.request.NativeGetUserRequest
import com.makentoshe.habrachan.network.response.NativeGetUserResponse

class NativeGetUserDeserializer : GsonDeserializer() {

    fun body(request: NativeGetUserRequest, json: String): Result<NativeGetUserResponse> = try {
        Result.success(gson.fromJson(json, NativeGetUserResponse.Factory::class.java).build(request))
    } catch (exception: Exception) {
        Result.failure(exception)
    }

    fun error(request: NativeGetUserRequest, json: String): Result<NativeGetUserResponse> {
        val factory = gson.fromJson(json, NativeGetUserDeserializerException.Factory::class.java)
        return Result.failure(factory.build(request, json))
    }
}