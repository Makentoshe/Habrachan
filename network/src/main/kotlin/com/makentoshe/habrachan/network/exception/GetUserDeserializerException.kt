package com.makentoshe.habrachan.network.exception

import com.makentoshe.habrachan.network.request.GetUserRequest

abstract class GetUserDeserializerException: Throwable() {
    abstract val request: GetUserRequest
    /** Full unparsed error */
    abstract val raw: String
}