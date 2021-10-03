package com.makentoshe.habrachan.network.exception

import com.makentoshe.habrachan.network.request.GetArticleRequest2

abstract class GetArticleException: Throwable() {
    abstract val request: GetArticleRequest2

    /** Might be null if exception occurs before any result received, e.g. SslHandshakeException */
    abstract val raw: String?

    override fun toString(): String {
        return "GetArticleException(request=${request}, raw=$raw)"
    }
}