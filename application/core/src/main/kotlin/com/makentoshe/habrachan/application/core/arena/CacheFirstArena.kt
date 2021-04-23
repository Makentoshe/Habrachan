package com.makentoshe.habrachan.application.core.arena

/**
 * Arena tries to get data from storage first. If storage does not contains element by the key [K]
 * the data will be fetched from the source and save in the storage for the future reuse.
 */
abstract class CacheFirstArena<in K, V>(private val arenaStorage: ArenaCache<K, V>) : Arena<K, V>() {

    override suspend fun suspendFetch(key: K): Result<V> {
        return arenaStorage.fetch(key).fold({ storageFetch ->
            Result.success(storageFetch)
        }, { storageThrowable ->
            internalSuspendFetch(key).fold({ sourceFetch ->
                arenaStorage.carry(key, sourceFetch)
                Result.success(sourceFetch)
            }, { sourceThrowable ->
                Result.failure(ArenaException(sourceException = sourceThrowable, cacheException = storageThrowable))
            })
        })
    }
}