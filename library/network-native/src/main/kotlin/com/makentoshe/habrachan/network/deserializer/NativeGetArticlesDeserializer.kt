package com.makentoshe.habrachan.network.deserializer

import com.google.gson.*
import com.makentoshe.habrachan.entity.ArticleText
import com.makentoshe.habrachan.network.request.GetArticlesRequest2
import com.makentoshe.habrachan.network.response.NativeGetArticlesResponse
import java.lang.reflect.Type

class NativeGetArticlesDeserializer : GsonDeserializer() {

    fun body(request: GetArticlesRequest2, json: String): Result<NativeGetArticlesResponse>  = try {
        Result.success(gson.fromJson(json, NativeGetArticlesResponse.Factory::class.java).build(request))
    } catch (exception: Exception) {
        Result.failure(exception)
    }

    fun error(request: GetArticlesRequest2, json: String): Result<NativeGetArticlesResponse> {
        return Result.failure(Exception(json))
    }
}
