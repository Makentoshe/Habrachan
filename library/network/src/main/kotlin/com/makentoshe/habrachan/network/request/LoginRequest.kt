package com.makentoshe.habrachan.network.request

/** Request user login data  */
data class LoginRequest(val client: String, val api: String, val email: String, val password: String)