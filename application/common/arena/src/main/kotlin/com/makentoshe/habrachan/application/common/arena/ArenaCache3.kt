package com.makentoshe.habrachan.application.common.arena

import com.makentoshe.habrachan.functional.Either2

/**
 * Performs main storage operation for the arenas
 */
interface ArenaCache3<K, V> {

    /**
     * Fetch result from the storage.
     * If could not return record by the key - returns [Either2.Right] with the [ArenaStorageException]
     */
    fun fetch(key: K): Either2<V, ArenaStorageException>

    /**
     * Save result to the storage.
     */
    fun carry(key: K, value: V)
}