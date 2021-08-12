package com.makentoshe.habrachan.network

import com.makentoshe.habrachan.network.exception.GetArticlesException

data class NativeGetArticlesException(
    override val request: NativeGetArticlesRequest,
    override val raw: String? = null,
    override val message: String? = null,
    override val cause: Throwable? = null,
) : GetArticlesException()