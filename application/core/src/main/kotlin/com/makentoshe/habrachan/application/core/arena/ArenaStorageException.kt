package com.makentoshe.habrachan.application.core.arena

/**
 * Cause in ArenaStorage operations.
 * If cause property is null - there are successful carry or fetch operation, but the result content is empty
 */
class ArenaStorageException(override val message: String) : Exception()