package com.makentoshe.habrachan.network.deserializer

import com.google.gson.Gson
import com.makentoshe.habrachan.network.request.MobileGetArticlesRequest
import com.makentoshe.habrachan.network.response.MobileGetArticlesResponse

class MobileGetArticlesDeserializer {

    fun body(request: MobileGetArticlesRequest, json: String): Result<MobileGetArticlesResponse> {
        return Result.success(Gson().fromJson(json, MobileGetArticlesResponse.Factory::class.java).build(request))
    }

    fun error(request: MobileGetArticlesRequest, json: String): Result<MobileGetArticlesResponse> {
        return Result.failure(Exception(json))
    }
}
