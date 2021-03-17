package com.makentoshe.habrachan.network.deserializer

import com.google.gson.Gson
import com.makentoshe.habrachan.network.response.NativeGetArticlesResponse

class NativeGetArticlesDeserializer {

    fun body(json: String): Result<NativeGetArticlesResponse> {
        return Result.success(Gson().fromJson(json, NativeGetArticlesResponse::class.java))
    }

    fun error(json: String): Result<NativeGetArticlesResponse> {
        return Result.failure(Exception(json))
    }
}
