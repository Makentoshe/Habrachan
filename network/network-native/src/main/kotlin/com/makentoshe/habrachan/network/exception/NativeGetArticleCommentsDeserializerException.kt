package com.makentoshe.habrachan.network.exception

import com.makentoshe.habrachan.network.request.NativeGetArticleCommentsRequest

class NativeGetArticleCommentsDeserializerException(
    override val request: NativeGetArticleCommentsRequest, override val raw: String
) : GetArticleCommentsDeserializerException()