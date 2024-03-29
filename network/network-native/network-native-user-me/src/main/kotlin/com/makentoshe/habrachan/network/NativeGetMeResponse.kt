package com.makentoshe.habrachan.network

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.entity.natives.User

data class NativeGetMeResponse(
    val request: NativeGetMeRequest, val user: User, val serverTime: String
) {

    class Factory(
        @SerializedName("data")
        val user: User,
        @SerializedName("server_time")
        val serverTime: String
    ) {
        fun build(request: NativeGetMeRequest) = NativeGetMeResponse(request, user, serverTime)
    }
}