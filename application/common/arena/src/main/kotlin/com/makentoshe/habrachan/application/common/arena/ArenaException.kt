package com.makentoshe.habrachan.application.common.arena

class ArenaException(
    val sourceException: Throwable? = null,
    val cacheException: Throwable? = null,
    override val message: String? = null,
) : Throwable() {

    override val cause: Throwable?
        get() = sourceException ?: cacheException

    override fun toString(): String {
        return "{\nsource=$sourceException,\ncache=$cacheException,\nmessage=$message\n}"
    }
}