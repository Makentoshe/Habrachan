package com.makentoshe.habrachan.common.model.network.login

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.common.model.entity.Additional

data class LoginResult(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("access_token")
    val accessToken: String? = null,
    @SerializedName("server_time")
    val serverTime: String? = null,
    @SerializedName("additional")
    val additional: Additional? = null,
    @SerializedName("code")
    val code: Int? = null,
    @SerializedName("message")
    val message: String? = null
)