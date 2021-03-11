package com.makentoshe.habrachan.network.deserializer

import com.google.gson.Gson
import com.makentoshe.habrachan.entity.mobiles.User
import com.makentoshe.habrachan.network.request.MobileGetUserRequest
import com.makentoshe.habrachan.network.response.MobileGetUserResponse

class MobileGetUserDeserializer {

    fun body(request: MobileGetUserRequest, json: String): Result<MobileGetUserResponse> = try {
        val user = Gson().fromJson(json, User::class.java)
        Result.success(MobileGetUserResponse(request, user))
    } catch (exception: Exception) {
        Result.failure(exception)
    }

    fun error(request: MobileGetUserRequest, json: String): Result<MobileGetUserResponse> {
        return Result.failure(Exception(json))
    }
}