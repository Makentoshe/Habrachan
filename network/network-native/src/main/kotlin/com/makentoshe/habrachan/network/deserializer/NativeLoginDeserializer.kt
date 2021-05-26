package com.makentoshe.habrachan.network.deserializer

import com.makentoshe.habrachan.network.exception.NativeLoginResponseException
import com.makentoshe.habrachan.network.request.NativeLoginRequest
import com.makentoshe.habrachan.network.response.LoginResponse

class NativeLoginDeserializer : GsonDeserializer() {

    fun body(json: String): Result<LoginResponse.NativeResponse> = try {
        Result.success(gson.fromJson(json, LoginResponse.NativeResponse::class.java))
    } catch (exception: Exception) {
        Result.failure(exception)
    }

    fun error(request: NativeLoginRequest, json: String): Result<LoginResponse> {
        val exception = gson.fromJson(json, NativeLoginResponseException.Factory::class.java).build(request, json)
        return Result.failure(exception)
    }
}