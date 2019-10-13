package com.makentoshe.habrachan.common.model.network.users

import com.google.gson.annotations.SerializedName
import com.makentoshe.habrachan.common.entity.User

data class GetUserByLoginResult(
    @SerializedName("data")
    val data: User,
    @SerializedName("server_time")
    val serverTime: String
)
