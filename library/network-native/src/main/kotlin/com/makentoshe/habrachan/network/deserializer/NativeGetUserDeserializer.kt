package com.makentoshe.habrachan.network.deserializer

import com.google.gson.Gson
import com.makentoshe.habrachan.network.exception.GetUserDeserializerException
import com.makentoshe.habrachan.network.exception.NativeGetUserDeserializerException
import com.makentoshe.habrachan.network.request.NativeGetUserRequest
import com.makentoshe.habrachan.network.response.NativeGetUserResponse

class NativeGetUserDeserializer {

    fun body(request: NativeGetUserRequest, json: String): Result<NativeGetUserResponse> {
        return Result.success(Gson().fromJson(json, NativeGetUserResponse.Factory::class.java).build(request))
    }

    fun error(request: NativeGetUserRequest, json: String): Result<NativeGetUserResponse> {
        val factory = Gson().fromJson(json, NativeGetUserDeserializerException.Factory::class.java)
        return Result.failure(factory.build(request, json))
    }
}