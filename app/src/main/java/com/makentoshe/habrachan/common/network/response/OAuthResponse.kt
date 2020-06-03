package com.makentoshe.habrachan.common.network.response

sealed class OAuthResponse {
    data class Success(
        val redirectUrl: String,
        val state: String
    ) : OAuthResponse()
}