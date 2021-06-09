package com.makentoshe.habrachan.network.deserializer

import com.makentoshe.habrachan.network.request.NativeGetMeRequest
import com.makentoshe.habrachan.network.response.NativeGetMeResponse

class NativeGetMeDeserializer : NativeGsonDeserializer() {

    fun body(request: NativeGetMeRequest, json: String): Result<NativeGetMeResponse> = try {
        Result.success(gson.fromJson(json, NativeGetMeResponse.Factory::class.java).build(request))
    } catch (exception: Exception) {
        Result.failure(exception)
    }

    // TODO(medium) Add proper deserialization
    fun error(request: NativeGetMeRequest, json: String): Result<NativeGetMeResponse> {
        return Result.failure(Exception(json))
    }
}