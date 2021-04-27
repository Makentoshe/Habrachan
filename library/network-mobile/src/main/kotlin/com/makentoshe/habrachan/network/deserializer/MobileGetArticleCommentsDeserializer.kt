package com.makentoshe.habrachan.network.deserializer

import com.google.gson.Gson
import com.makentoshe.habrachan.entity.mobiles.User
import com.makentoshe.habrachan.network.exceptions.MobileGetUserDeserializerException
import com.makentoshe.habrachan.network.request.MobileGetArticleCommentsRequest
import com.makentoshe.habrachan.network.request.MobileGetUserRequest
import com.makentoshe.habrachan.network.response.MobileGetArticleCommentsResponse
import com.makentoshe.habrachan.network.response.MobileGetUserResponse

class MobileGetArticleCommentsDeserializer {

    fun body(request: MobileGetArticleCommentsRequest, json: String): Result<MobileGetArticleCommentsResponse> = try {
        val user = Gson().fromJson(json, User::class.java)
        Result.success(MobileGetUserResponse(request, user))
    } catch (exception: Exception) {
        Result.failure(exception)
    }

    fun error(request: MobileGetArticleCommentsResponse, json: String): Result<MobileGetArticleCommentsResponse> {
        return Result.failure(MobileGetUserDeserializerException(request, json))
    }
}