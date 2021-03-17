package com.makentoshe.habrachan.network.deserializer

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.makentoshe.habrachan.network.response.NativeGetArticleResponse

open class NativeGetArticleDeserializer {

    // article_success
    open fun body(json: String): Result<NativeGetArticleResponse.Factory> = try {
        Result.success(Gson().fromJson(json, NativeGetArticleResponse.Factory::class.java))
    } catch (jse: JsonSyntaxException) {
        Result.failure(jse) //article_failure
    }

    open fun error(json: String): Result<NativeGetArticleResponse.Factory> {
        return Result.failure(Exception(json))
    }
}