package com.makentoshe.habrachan.network.exceptions

import com.makentoshe.habrachan.network.request.MobileGetArticleCommentsRequest

class MobileGetArticleCommentsManagerException(
    val request: MobileGetArticleCommentsRequest, override val cause: Throwable
) : Throwable()