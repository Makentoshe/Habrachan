package com.makentoshe.habrachan.network.deserializer

import com.google.gson.Gson
import com.makentoshe.habrachan.network.request.GetArticlesRequest2
import com.makentoshe.habrachan.network.response.NativeGetArticlesResponse

class NativeGetArticlesDeserializer {

    fun body(request: GetArticlesRequest2, json: String): Result<NativeGetArticlesResponse> {
        return Result.success(Gson().fromJson(json, NativeGetArticlesResponse.Factory::class.java).build(request))
    }

    fun error(request: GetArticlesRequest2, json: String): Result<NativeGetArticlesResponse> {
        return Result.failure(Exception(json))
    }
}
