package com.makentoshe.habrachan.network.exception

import com.makentoshe.habrachan.network.request.GetCommentsRequest2

abstract class GetCommentsDeserializerException : Throwable() {
    abstract val request: GetCommentsRequest2

    /** Full unparsed error */
    abstract val raw: String
}