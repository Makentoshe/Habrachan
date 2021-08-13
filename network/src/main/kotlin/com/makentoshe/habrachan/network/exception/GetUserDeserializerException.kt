package com.makentoshe.habrachan.network.exception

import com.makentoshe.habrachan.network.request.GetUserRequest

abstract class GetUserDeserializerException : Throwable() {
    abstract val request: GetUserRequest

    /**
     * Full unparsed error.
     * Might be null if exception caused before any data received
     */
    abstract val raw: String?
}