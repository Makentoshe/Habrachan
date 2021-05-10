package com.makentoshe.habrachan.network.exceptions

import com.makentoshe.habrachan.network.request.MobileGetArticleCommentsRequest

class MobileGetArticleCommentsDeserializerException(
    val request: MobileGetArticleCommentsRequest,
    val json: String
): Throwable()