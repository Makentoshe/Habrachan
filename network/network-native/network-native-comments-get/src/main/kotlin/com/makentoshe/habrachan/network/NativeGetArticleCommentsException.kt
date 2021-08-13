package com.makentoshe.habrachan.network

import com.makentoshe.habrachan.network.exception.GetArticleCommentsDeserializerException

class NativeGetArticleCommentsException(
    override val request: NativeGetArticleCommentsRequest,
    override val raw: String? = null,
    override val message: String? = null,
    override val cause: Throwable? = null,
) : GetArticleCommentsDeserializerException()