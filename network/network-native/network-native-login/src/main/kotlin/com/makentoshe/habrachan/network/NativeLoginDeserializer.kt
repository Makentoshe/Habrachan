package com.makentoshe.habrachan.network

import com.makentoshe.habrachan.functional.Result
import com.makentoshe.habrachan.network.response.LoginResponse

class NativeLoginDeserializer : NativeGsonDeserializer() {

    fun body(json: String): Result<LoginResponse.NativeResponse> = try {
        Result.success(gson.fromJson(json, LoginResponse.NativeResponse::class.java))
    } catch (exception: Exception) {
        Result.failure(exception)
    }

    fun error(request: NativeLoginRequest, json: String): Result<LoginResponse> {
        val exception = gson.fromJson(json, NativeLoginException.Factory::class.java).build(request, json)
        return Result.failure(exception)
    }
}