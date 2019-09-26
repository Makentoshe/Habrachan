package com.makentoshe.habrachan.common.model.network.login

data class LoginRequest(
    val email: String,
    val password: String,
    // if not null - login using default api
    val clientKey: String? = null,
    // if not null - login using default api
    val apiKey: String? = null,
    // if not null - login using default api
    val clientSecret: String? = null
)