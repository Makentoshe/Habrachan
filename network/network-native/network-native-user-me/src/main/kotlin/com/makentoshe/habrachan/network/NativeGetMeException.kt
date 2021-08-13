package com.makentoshe.habrachan.network

data class NativeGetMeException(
    val request: NativeGetMeRequest,
    val raw: String? = null,
    override val message: String? = null,
    override val cause: Throwable? = null,
) : Throwable()