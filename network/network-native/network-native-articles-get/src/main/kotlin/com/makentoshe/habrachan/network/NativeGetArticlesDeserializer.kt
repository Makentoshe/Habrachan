package com.makentoshe.habrachan.network

import com.makentoshe.habrachan.functional.Result
import com.makentoshe.habrachan.network.deserializer.NativeGsonDeserializer
import com.makentoshe.habrachan.network.request.GetArticlesRequest2

class NativeGetArticlesDeserializer : NativeGsonDeserializer() {

    fun body(request: GetArticlesRequest2, json: String): Result<NativeGetArticlesResponse>  = try {
        Result.success(gson.fromJson(json, NativeGetArticlesResponse.Factory::class.java).build(request))
    } catch (exception: Exception) {
        Result.failure(exception)
    }

    fun error(request: GetArticlesRequest2, json: String): Result<NativeGetArticlesResponse> {
        return Result.failure(Exception(json))
    }
}
