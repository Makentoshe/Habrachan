package com.makentoshe.habrachan.network.response

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.entity.natives.User

data class UserResponse(
    @SerializedName("data")
    val user: User,
    @SerializedName("server_time")
    val serverTime: String
)