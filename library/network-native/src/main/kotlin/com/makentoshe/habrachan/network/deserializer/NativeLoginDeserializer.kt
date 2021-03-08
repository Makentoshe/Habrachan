package com.makentoshe.habrachan.network.deserializer

import com.google.gson.Gson
import com.makentoshe.habrachan.network.response.LoginResponse2

class NativeLoginDeserializer {

    fun body(json: String): Result<LoginResponse2.NativeResponse> {
        return Result.success(Gson().fromJson(json, LoginResponse2.NativeResponse::class.java))
    }

    fun error(json: String): Result<LoginResponse2.NativeResponse> {
        return Result.failure(Exception(json))
    }
}