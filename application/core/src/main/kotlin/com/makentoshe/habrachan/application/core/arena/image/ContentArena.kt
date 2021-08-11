package com.makentoshe.habrachan.application.core.arena.image

import com.makentoshe.habrachan.application.core.arena.ArenaCache
import com.makentoshe.habrachan.application.core.arena.CacheFirstArena
import com.makentoshe.habrachan.functional.fold
import com.makentoshe.habrachan.network.GetContentManager
import com.makentoshe.habrachan.network.GetContentRequest
import com.makentoshe.habrachan.network.GetContentResponse

class ContentArena(
    val manager: GetContentManager,
    cache: ArenaCache<GetContentRequest, GetContentResponse>
) : CacheFirstArena<GetContentRequest, GetContentResponse>(cache) {
    override suspend fun internalSuspendFetch(key: GetContentRequest): kotlin.Result<GetContentResponse> {
        return manager.content(key).fold({ kotlin.Result.success(it) }, { kotlin.Result.failure(it) })
    }
}