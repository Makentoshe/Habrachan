package com.makentoshe.habrachan.mobiles.network.deserializer

import com.google.gson.Gson
import com.makentoshe.habrachan.mobiles.network.response.MobileGetArticlesResponse

class MobileGetArticlesDeserializer {

    fun body(json: String): Result<MobileGetArticlesResponse> {
        return Result.success(Gson().fromJson(json, MobileGetArticlesResponse::class.java))
    }

    fun error(json: String): Result<MobileGetArticlesResponse> {
        return Result.failure(Exception(json))
    }
}
