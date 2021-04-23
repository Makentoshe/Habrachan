package com.makentoshe.habrachan.network.deserializer

import com.google.gson.Gson
import com.makentoshe.habrachan.network.request.NativeGetMeRequest
import com.makentoshe.habrachan.network.response.NativeGetMeResponse

class NativeGetMeDeserializer {

    fun body(request: NativeGetMeRequest, json: String): Result<NativeGetMeResponse> {
        return Result.success(Gson().fromJson(json, NativeGetMeResponse.Factory::class.java).build(request))
    }

    // TODO(medium) Add proper deserialization
    fun error(request: NativeGetMeRequest, json: String): Result<NativeGetMeResponse> {
        return Result.failure(Exception(json))
    }
}