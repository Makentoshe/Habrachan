package com.makentoshe.habrachan.common.model.network.users

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.common.model.entity.User

data class GetUserByLoginResult(
    @SerializedName("data")
    val data: User? = null,
    @SerializedName("server_time")
    val serverTime: String? = null,
    @SerializedName("code")
    val code: Int? = null,
    @SerializedName("message")
    val message: String? = null,

    val success: Boolean = data != null
)
