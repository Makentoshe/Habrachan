package com.makentoshe.habrachan.application.common.arena

import com.makentoshe.habrachan.functional.Either
import com.makentoshe.habrachan.functional.Result

/**
 * Performs main storage operation for the arenas
 */
interface ArenaCache<K, V> {

    /**
     * Fetch result from the storage.
     * If could not return record by the key - returns [Result] with the [ArenaStorageException]
     */
    fun fetch(key: K): Result<V>

    /**
     * Save result to the storage.
     */
    fun carry(key: K, value: V)
}

/**
 * Performs main storage operation for the arenas
 */
interface ArenaCache2<K, V> {

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
