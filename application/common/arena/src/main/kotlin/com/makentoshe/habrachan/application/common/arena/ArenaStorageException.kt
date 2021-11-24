package com.makentoshe.habrachan.application.common.arena

/** Cause in ArenaStorage operations */
open class ArenaStorageException(
    override val message: String,
    override val cause: Throwable? = null,
) : Exception() {
    constructor(cause: Throwable?) : this(cause?.message ?: "", cause)
}