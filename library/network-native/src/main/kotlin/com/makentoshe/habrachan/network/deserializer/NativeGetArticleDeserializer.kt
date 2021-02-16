package com.makentoshe.habrachan.network.deserializer

import com.google.gson.Gson
import com.makentoshe.habrachan.network.response.NativeGetArticleResponse

open class NativeGetArticleDeserializer {

    open fun body(json: String): Result<NativeGetArticleResponse.Factory> {
        return Result.success(Gson().fromJson(json, NativeGetArticleResponse.Factory::class.java))
    }

    open fun error(json: String): Result<NativeGetArticleResponse.Factory> {
        return Result.failure(Exception(json))
    }
}