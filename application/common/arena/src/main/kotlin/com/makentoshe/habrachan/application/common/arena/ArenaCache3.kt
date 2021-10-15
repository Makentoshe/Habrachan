package com.makentoshe.habrachan.application.common.arena

import com.makentoshe.habrachan.functional.Either

/**
 * Performs main storage operation for the arenas
 */
interface ArenaCache3<K, V> {

    /**
     * Fetch result from the storage.
     * If could not return record by the key - returns [Either.Right] with the [ArenaStorageException]
     */
    fun fetch(key: K): Either<V, ArenaStorageException>

    /**
     * Save result to the storage.
     */
    fun carry(key: K, value: V)
}