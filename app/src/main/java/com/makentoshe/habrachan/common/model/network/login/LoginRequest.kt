package com.makentoshe.habrachan.common.model.network.login

data class LoginRequest(
    val email: String,
    val password: String,
    val clientKey: String,
    val apiKey: String,
    val clientSecret: String
)