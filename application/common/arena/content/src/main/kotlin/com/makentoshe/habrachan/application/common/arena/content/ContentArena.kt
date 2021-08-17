package com.makentoshe.habrachan.application.common.arena.content

import com.makentoshe.habrachan.application.common.arena.Arena
import com.makentoshe.habrachan.application.common.arena.ArenaCache
import com.makentoshe.habrachan.network.GetContentManager
import com.makentoshe.habrachan.network.GetContentRequest
import com.makentoshe.habrachan.network.GetContentResponse
import com.makentoshe.habrachan.network.UserSession
import javax.inject.Inject

abstract class ContentArena internal constructor(
    private val manager: GetContentManager,
) : Arena<GetContentRequest, GetContentResponse>() {

    abstract fun request(userSession: UserSession, url: String): GetContentRequest

    override suspend fun internalSuspendFetch(key: GetContentRequest) = manager.content(key)

    class Factory @Inject constructor(
        private val manager: GetContentManager,
        private val arenaStorage: ArenaCache<GetContentRequest, GetContentResponse>,
    ) {
        fun sourceFirstArena(): ContentArena {
            return SourceFirstContentArena(manager, arenaStorage)
        }

        fun cacheFirstArena(): ContentArena {
            return CacheFirstContentArena(manager, arenaStorage)
        }
    }
}


