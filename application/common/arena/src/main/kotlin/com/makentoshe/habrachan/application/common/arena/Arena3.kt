package com.makentoshe.habrachan.application.common.arena

import com.makentoshe.habrachan.functional.Either2

abstract class Arena3<in K, V> {

    protected abstract val arenaStorage: ArenaCache3<in K, V>

    /** Main method for performing network operation */
    abstract suspend fun internalSuspendFetch(key: K): Either2<V, Throwable>

    protected suspend fun sourceFirstSuspendFetch(key: K) = internalSuspendFetch(key).fold({ sourceFetch ->
        arenaStorage.carry(key, sourceFetch)
        Either2.Left(sourceFetch)
    }, { sourceThrowable ->
        arenaStorage.fetch(key).fold({ cacheFetch ->
            Either2.Left(cacheFetch)
        }, { cacheThrowable ->
            Either2.Right(ArenaException(sourceException = sourceThrowable, cacheException = cacheThrowable))
        })
    })

    protected suspend fun cacheFirstSuspendFetch(key: K): Either2<V, ArenaException> {
        return arenaStorage.fetch(key).fold({ storageFetch ->
            Either2.Left(storageFetch)
        }, { storageThrowable ->
            internalSuspendFetch(key).fold({ sourceFetch ->
                arenaStorage.carry(key, sourceFetch)
                Either2.Left(sourceFetch)
            }, { sourceThrowable ->
                Either2.Right(ArenaException(sourceException = sourceThrowable, cacheException = storageThrowable))
            })
        })
    }

    /** Method executes network and caches operations */
    abstract suspend fun suspendFetch(key: K): Either2<V, ArenaException>
}