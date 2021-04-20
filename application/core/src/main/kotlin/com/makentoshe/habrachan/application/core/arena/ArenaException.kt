package com.makentoshe.habrachan.application.core.arena

class ArenaException(
    val sourceException: Throwable? = null, val cacheException: Throwable? = null, override val message: String? = null
) : Throwable() {
    override fun toString(): String {
        return "{\nsource=$sourceException,\ncache=$cacheException,\nmessage=$message\n}"
    }
}