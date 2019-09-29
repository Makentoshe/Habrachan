package com.makentoshe.habrachan.common.model.network.login

import com.google.gson.annotations.SerializedName

data class LoginResult(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("server_time")
    val serverTime: String
)