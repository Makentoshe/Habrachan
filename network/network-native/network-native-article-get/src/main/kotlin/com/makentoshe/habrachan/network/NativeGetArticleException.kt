package com.makentoshe.habrachan.network

class NativeGetArticleException(
    val request: NativeGetArticleRequest,
    override val message: String? = null,
    override val cause: Throwable? = null,
) : Exception()
