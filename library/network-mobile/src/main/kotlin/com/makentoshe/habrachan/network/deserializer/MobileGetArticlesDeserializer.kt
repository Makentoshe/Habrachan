package com.makentoshe.habrachan.network.deserializer

import com.google.gson.Gson
import com.makentoshe.habrachan.network.response.MobileGetArticlesResponse

class MobileGetArticlesDeserializer {

    fun body(json: String): Result<MobileGetArticlesResponse> {
        return Result.success(Gson().fromJson(json, MobileGetArticlesResponse::class.java))
    }

    fun error(json: String): Result<MobileGetArticlesResponse> {
        return Result.failure(Exception(json))
    }
}
