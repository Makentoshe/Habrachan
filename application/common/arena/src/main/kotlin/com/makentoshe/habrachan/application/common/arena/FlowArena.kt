package com.makentoshe.habrachan.application.common.arena

import com.makentoshe.habrachan.functional.Either2
import kotlinx.coroutines.flow.flow

data class FlowArenaResponse<V>(val loading: Boolean, val content: Either2<V, ArenaException>)

abstract class FlowArena<in K, V> {

    protected abstract val arenaStorage: ArenaCache3<in K, V>

    protected abstract suspend fun suspendSourceFetch(key: K): Either2<V, Throwable>

    suspend fun suspendFlowFetch(key: K) = flow {
        emit(FlowArenaResponse(loading = true, content = suspendStorageFetch(key)))
        emit(FlowArenaResponse(loading = false, content = suspendSourceFetch(key).mapRight(::ArenaException)))
    }

    protected fun suspendStorageFetch(key: K) = arenaStorage.fetch(key).mapRight { arenaStorageException ->
        ArenaException(cacheException = arenaStorageException)
    }
}