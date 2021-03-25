package com.makentoshe.habrachan.network.deserializer

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.makentoshe.habrachan.network.exception.NativeLoginResponseException
import com.makentoshe.habrachan.network.request.LoginRequest
import com.makentoshe.habrachan.network.request.NativeLoginRequest
import com.makentoshe.habrachan.network.response.LoginResponse

class NativeLoginDeserializer {

    fun body(json: String): Result<LoginResponse.NativeResponse> {
        return Result.success(Gson().fromJson(json, LoginResponse.NativeResponse::class.java))
    }

    fun error(request: NativeLoginRequest, json: String): Result<LoginResponse> {
        val exception = Gson().fromJson(json, NativeLoginResponseException.Factory::class.java).build(request, json)
        return Result.failure(exception)
    }
}