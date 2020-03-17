package com.makentoshe.habrachan.common.entity.user

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.common.entity.User

sealed class UserResponse {

    class Success(
        @SerializedName("data")
        val user: User,
        @SerializedName("server_time")
        val serverTime: String
    ): UserResponse()

    class Error(json: String): UserResponse()
}