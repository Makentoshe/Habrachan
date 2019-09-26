package com.makentoshe.habrachan.common.model.network.login

import com.google.gson.annotations.SerializedName

data class LoginResult(
    @SerializedName("success")
    val success: Boolean = true,
    @SerializedName("access_token")
    val accessToken: String? = null,
    @SerializedName("server_time")
    val serverTime: String? = null,
    val message: String? = null,
    val code: Int? = null
)