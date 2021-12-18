package com.makentoshe.habrachan.application.common.arena

import com.makentoshe.habrachan.functional.Either2
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class FlowArenaResponse<V, E>(val loading: Boolean, val content: Either2<V, E>)

abstract class FlowArena<in K, V> {

    protected abstract val arenaStorage: ArenaCache3<in K, V>

    protected abstract suspend fun suspendSourceFetch(key: K): Either2<V, Throwable>

    suspend fun suspendFlowFetch(key: K): Flow<FlowArenaResponse<V, ArenaException>> = flow {
        emit(FlowArenaResponse(loading = true, content = suspendStorageFetch(key)))

        val sourceContent = suspendSourceFetch(key).mapRight(::ArenaException).onLeft { arenaStorage.carry(key, it) }
        emit(FlowArenaResponse(loading = false, content = sourceContent))
    }

    protected fun suspendStorageFetch(key: K) = arenaStorage.fetch(key).mapRight { arenaStorageException ->
        ArenaException(cacheException = arenaStorageException)
    }
}