package com.makentoshe.habrachan.network.deserializer

import com.makentoshe.habrachan.entity.mobiles.User
import com.makentoshe.habrachan.functional.Result
import com.makentoshe.habrachan.network.exceptions.MobileGetUserDeserializerException
import com.makentoshe.habrachan.network.request.MobileGetUserRequest
import com.makentoshe.habrachan.network.response.MobileGetUserResponse

class MobileGetUserDeserializer : MobileGsonDeserializer() {

    fun body(request: MobileGetUserRequest, json: String): Result<MobileGetUserResponse> = try {
        val user = gson.fromJson(json, User::class.java)
        Result.success(MobileGetUserResponse(request, user))
    } catch (exception: Exception) {
        Result.failure(exception)
    }

    fun error(request: MobileGetUserRequest, json: String): Result<MobileGetUserResponse> {
        return Result.failure(MobileGetUserDeserializerException(request, json))
    }
}