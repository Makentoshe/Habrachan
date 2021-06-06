package com.makentoshe.habrachan.network.login

data class WebMobileLoginException(
    val request: WebMobileLoginRequest,
    override val cause: Throwable? = null
): Throwable()