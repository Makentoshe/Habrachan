package com.makentoshe.habrachan.application.common.arena

/** Cause in ArenaStorage operations */
class ArenaStorageException(
    override val message: String,
    override val cause: Throwable? = null,
) : Exception()