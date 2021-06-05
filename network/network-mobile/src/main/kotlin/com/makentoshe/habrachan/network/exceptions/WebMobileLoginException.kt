package com.makentoshe.habrachan.network.exceptions

import com.makentoshe.habrachan.network.request.WebMobileLoginRequest

data class WebMobileLoginException(
    val request: WebMobileLoginRequest,
    override val cause: Throwable? = null
): Throwable()

data class WebMobuleLoginDeserializeException(
    val request: WebMobileLoginRequest,
    val raw: String,
    override val cause: Throwable? = null
): Throwable()