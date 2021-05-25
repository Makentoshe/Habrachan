package com.makentoshe.habrachan.network.deserializer

import com.makentoshe.habrachan.network.request.MobileGetArticlesRequest
import com.makentoshe.habrachan.network.response.MobileGetArticlesResponse

class MobileGetArticlesDeserializer : GsonDeserializer() {

    fun body(request: MobileGetArticlesRequest, json: String): Result<MobileGetArticlesResponse> = try {
        Result.success(gson.fromJson(json, MobileGetArticlesResponse.Factory::class.java).build(request))
    } catch (exception: Exception) {
        Result.failure(exception)
    }

    fun error(request: MobileGetArticlesRequest, json: String): Result<MobileGetArticlesResponse> {
        return Result.failure(Exception(json))
    }
}
