package com.makentoshe.habrachan.common.entity.user

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.common.entity.User

data class UserResponse(
    @SerializedName("data")
    val user: User,
    @SerializedName("server_time")
    val serverTime: String
)