package com.makentoshe.habrachan.network.deserializer

import com.google.gson.Gson
import com.makentoshe.habrachan.network.exception.NativeGetCommentsDeserializerException
import com.makentoshe.habrachan.network.request.NativeGetCommentsRequest
import com.makentoshe.habrachan.network.response.NativeGetCommentsResponse

class NativeGetCommentsDeserializer {

    fun body(request: NativeGetCommentsRequest, json: String): Result<NativeGetCommentsResponse> {
        return Result.success(Gson().fromJson(json, NativeGetCommentsResponse.Factory::class.java).build(request))
    }

    fun error(request: NativeGetCommentsRequest, json: String): Result<NativeGetCommentsResponse> {
        return Result.failure(NativeGetCommentsDeserializerException(request, json))
    }

}