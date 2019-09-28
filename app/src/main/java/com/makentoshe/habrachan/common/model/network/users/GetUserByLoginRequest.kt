package com.makentoshe.habrachan.common.model.network.users

data class GetUserByLoginRequest(
    val login: String,
    val clientKey: String,
    val apiKey: String?,
    val accessToken: String?
)