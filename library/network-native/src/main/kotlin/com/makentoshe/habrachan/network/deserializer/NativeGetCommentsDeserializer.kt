package com.makentoshe.habrachan.network.deserializer

import com.google.gson.Gson
import com.makentoshe.habrachan.network.exception.NativeGetArticleCommentsDeserializerException
import com.makentoshe.habrachan.network.request.NativeGetArticleCommentsRequest
import com.makentoshe.habrachan.network.response.NativeGetArticleCommentsResponse

class NativeGetCommentsDeserializer {

    fun body(request: NativeGetArticleCommentsRequest, json: String): Result<NativeGetArticleCommentsResponse> {
        return Result.success(Gson().fromJson(json, NativeGetArticleCommentsResponse.Factory::class.java).build(request))
    }

    fun error(request: NativeGetArticleCommentsRequest, json: String): Result<NativeGetArticleCommentsResponse> {
        return Result.failure(NativeGetArticleCommentsDeserializerException(request, json))
    }

}