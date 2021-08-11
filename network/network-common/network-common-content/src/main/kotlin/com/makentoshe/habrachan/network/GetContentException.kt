package com.makentoshe.habrachan.network

class GetContentException(
    val request: GetContentRequest,
    override val message: String? = null,
    override val cause: Throwable? = null,
) : Exception()
