package com.makentoshe.habrachan.application.core.arena

/**
 * Arena tries to load from network source first. If the network connection stable the
 * cache will be overwritten by the new result. If the network connection interrupted
 * and data exists in the cache it will be loaded.
 */
abstract class SourceFirstArena<in K, V>(private val arenaStorage: ArenaCache<K, V>) : Arena<K, V>() {

    override suspend fun suspendFetch(key: K): Result<V> {
        return internalSuspendFetch(key).fold({ sourceFetch ->
            arenaStorage.carry(key, sourceFetch)
            Result.success(sourceFetch)
        }, { sourceThrowable ->
            arenaStorage.fetch(key).fold({ cacheFetch ->
                Result.success(cacheFetch)
            }, { cacheThrowable ->
                Result.failure(ArenaException(sourceException = sourceThrowable, cacheException = cacheThrowable))
            })
        })
    }
}

