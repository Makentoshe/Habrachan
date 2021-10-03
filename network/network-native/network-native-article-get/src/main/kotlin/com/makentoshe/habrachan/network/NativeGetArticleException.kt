package com.makentoshe.habrachan.network

import com.makentoshe.habrachan.network.exception.GetArticleException

class NativeGetArticleException(
    override val request: NativeGetArticleRequest,
    override val raw: String? = null,
    override val message: String? = null,
    override val cause: Throwable? = null,
) : GetArticleException()
