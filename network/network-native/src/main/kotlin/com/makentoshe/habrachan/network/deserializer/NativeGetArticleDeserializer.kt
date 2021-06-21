package com.makentoshe.habrachan.network.deserializer

import com.makentoshe.habrachan.network.response.NativeGetArticleResponse

open class NativeGetArticleDeserializer: NativeGsonDeserializer() {

    // article_success
    open fun body(json: String): Result<NativeGetArticleResponse.Factory> = try {
        Result.success(gson.fromJson(json, NativeGetArticleResponse.Factory::class.java))
    } catch (exception: Exception) {
        Result.failure(exception) //article_failure
    }

    open fun error(json: String): Result<NativeGetArticleResponse.Factory> {
        return Result.failure(Exception(json))
    }
}