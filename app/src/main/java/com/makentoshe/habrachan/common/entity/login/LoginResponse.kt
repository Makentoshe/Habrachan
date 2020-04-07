package com.makentoshe.habrachan.common.entity.login

import com.google.gson.annotations.SerializedName

sealed class LoginResponse {

    data class Success(
        @SerializedName("access_token")
        val accessToken: String,
        @SerializedName("server_time")
        val serverTime: String
    ) : LoginResponse()

    data class Error(
        @SerializedName("additional")
        val additional: List<String>,
        @SerializedName("code")
        val code: Int,
        @SerializedName("message")
        val message: String
    ) : LoginResponse()

}