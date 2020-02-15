package com.makentoshe.habrachan.common.network.request

data class LoginRequest(
    val client: String,
    val api: String,
    val email: String,
    val password: String
) {
    class Builder(private val client: String, private val api: String) {
        fun build(email: String, password: String) = LoginRequest(client, api, email, password)
    }
}