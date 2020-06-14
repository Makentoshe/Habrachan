package com.makentoshe.habrachan.common.network.request

data class OAuthRequest(
    val clientId: String,
    val socialType: String,
    val hostUrl: String,
    val responseType: String = "token",
    val redirectUri: String = "http://cleverpumpkin.ru"
)
