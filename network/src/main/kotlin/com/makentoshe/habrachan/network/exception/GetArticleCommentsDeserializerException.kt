package com.makentoshe.habrachan.network.exception

import com.makentoshe.habrachan.network.request.GetArticleCommentsRequest

abstract class GetArticleCommentsDeserializerException : Throwable() {
    abstract val request: GetArticleCommentsRequest

    /** Full unparsed error */
    abstract val raw: String?
}