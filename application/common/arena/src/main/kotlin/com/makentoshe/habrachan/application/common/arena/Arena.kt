package com.makentoshe.habrachan.application.common.arena

import com.makentoshe.habrachan.functional.Result
import com.makentoshe.habrachan.functional.suspendFold

/**
 * Performs main operations with the data using network and cache sources
 */
abstract class Arena<in K, V> {

    protected abstract val arenaStorage: ArenaCache<in K, V>

    /** Main method for performing network operation */
    abstract suspend fun internalSuspendFetch(key: K): Result<V>

    protected suspend fun sourceFirstSuspendFetch(key: K) = internalSuspendFetch(key).suspendFold({ sourceFetch ->
        arenaStorage.carry(key, sourceFetch)
        Result.success(sourceFetch)
    }, { sourceThrowable ->
        arenaStorage.fetch(key).suspendFold({ cacheFetch ->
            Result.success(cacheFetch)
        }, { cacheThrowable ->
            Result.failure(ArenaException(sourceException = sourceThrowable, cacheException = cacheThrowable))
        })
    })

    protected suspend fun cacheFirstSuspendFetch(key: K): Result<V> {
        return arenaStorage.fetch(key).suspendFold({ storageFetch ->
            Result.success(storageFetch)
        }, { storageThrowable ->
            internalSuspendFetch(key).suspendFold({ sourceFetch ->
                arenaStorage.carry(key, sourceFetch)
                Result.success(sourceFetch)
            }, { sourceThrowable ->
                Result.failure(ArenaException(sourceException = sourceThrowable, cacheException = storageThrowable))
            })
        })
    }

    /** Method executes network and caches operations */
    abstract suspend fun suspendFetch(key: K): Result<V>
}