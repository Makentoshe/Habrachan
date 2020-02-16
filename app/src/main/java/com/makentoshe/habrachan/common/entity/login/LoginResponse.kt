package com.makentoshe.habrachan.common.entity.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("server_name")
    val serverTime: String
)