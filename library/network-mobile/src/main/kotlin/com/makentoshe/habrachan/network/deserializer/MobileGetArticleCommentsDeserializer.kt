package com.makentoshe.habrachan.network.deserializer

import com.google.gson.Gson
import com.makentoshe.habrachan.network.exceptions.MobileGetArticleCommentsDeserializerException
import com.makentoshe.habrachan.network.request.MobileGetArticleCommentsRequest
import com.makentoshe.habrachan.network.response.MobileGetArticleCommentsResponse

class MobileGetArticleCommentsDeserializer {

    fun body(request: MobileGetArticleCommentsRequest, json: String): Result<MobileGetArticleCommentsResponse> = try {
        val factory = Gson().fromJson(json, MobileGetArticleCommentsResponse.Factory::class.java)
        Result.success(factory.build(request))
    } catch (exception: Exception) {
        Result.failure(exception)
    }

    fun error(request: MobileGetArticleCommentsRequest, json: String): Result<MobileGetArticleCommentsResponse> {
        return Result.failure(MobileGetArticleCommentsDeserializerException(request, json))
    }
}