package com.makentoshe.habrachan.application.core.arena.image

import com.makentoshe.habrachan.application.core.arena.ArenaCache
import com.makentoshe.habrachan.application.core.arena.CacheFirstArena
import com.makentoshe.habrachan.network.manager.GetContentManager
import com.makentoshe.habrachan.network.request.GetContentRequest
import com.makentoshe.habrachan.network.response.GetContentResponse

class ContentArena(
    val manager: GetContentManager,
    cache: ArenaCache<GetContentRequest, GetContentResponse>
): CacheFirstArena<GetContentRequest, GetContentResponse>(cache) {
    override suspend fun internalSuspendFetch(key: GetContentRequest): Result<GetContentResponse> {
        return manager.content(key)
    }
}