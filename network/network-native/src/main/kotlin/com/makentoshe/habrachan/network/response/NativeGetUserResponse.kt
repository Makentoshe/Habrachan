package com.makentoshe.habrachan.network.response

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.entity.natives.User
import com.makentoshe.habrachan.network.request.NativeGetUserRequest

data class NativeGetUserResponse(
    override val request: NativeGetUserRequest, override val user: User, val serverTime: String
) : GetUserResponse {

    class Factory(
        @SerializedName("data")
        val user: User,
        @SerializedName("server_time")
        val serverTime: String
    ) {
        fun build(request: NativeGetUserRequest) = NativeGetUserResponse(request, user, serverTime)
    }
}