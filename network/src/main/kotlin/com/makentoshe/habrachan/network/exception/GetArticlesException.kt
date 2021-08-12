package com.makentoshe.habrachan.network.exception

import com.makentoshe.habrachan.network.request.GetArticlesRequest2

abstract class GetArticlesException : Throwable() {
    abstract val request: GetArticlesRequest2

    /** Might be null if exception occurs before any result received, e.g. SslHandshakeException */
    abstract val raw: String?
}