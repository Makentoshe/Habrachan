package com.makentoshe.habrachan.application.common.arena.content

import com.makentoshe.habrachan.application.common.arena.ArenaCache
import com.makentoshe.habrachan.network.GetContentManager
import com.makentoshe.habrachan.network.GetContentRequest
import com.makentoshe.habrachan.network.GetContentResponse
import com.makentoshe.habrachan.network.UserSession

internal class CacheFirstContentArena constructor(
    private val manager: GetContentManager,
    override val arenaStorage: ArenaCache<GetContentRequest, GetContentResponse>,
) : ContentArena(manager) {

    override fun request(userSession: UserSession, url: String) = manager.request(userSession, url)

    override suspend fun suspendFetch(key: GetContentRequest) = cacheFirstSuspendFetch(key)

}