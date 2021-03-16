package com.makentoshe.habrachan.network.deserializer

import com.google.gson.Gson
import com.makentoshe.habrachan.network.response.LoginResponse

class NativeLoginDeserializer {

    fun body(json: String): Result<LoginResponse.NativeResponse> {
        return Result.success(Gson().fromJson(json, LoginResponse.NativeResponse::class.java))
    }

    fun error(json: String): Result<LoginResponse.NativeResponse> {
        return Result.failure(Exception(json))
    }
}