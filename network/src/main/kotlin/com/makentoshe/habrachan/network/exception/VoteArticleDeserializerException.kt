package com.makentoshe.habrachan.network.exception

import com.makentoshe.habrachan.network.request.VoteArticleRequest

abstract class VoteArticleDeserializerException : Throwable() {
    abstract val request: VoteArticleRequest
    /** Full unparsed error */
    abstract val raw: String
}
