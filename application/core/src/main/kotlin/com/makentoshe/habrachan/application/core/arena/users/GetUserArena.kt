package com.makentoshe.habrachan.application.core.arena.users

import com.makentoshe.habrachan.application.core.arena.ArenaCache
import com.makentoshe.habrachan.application.core.arena.SourceFirstArena
import com.makentoshe.habrachan.network.manager.GetUserManager
import com.makentoshe.habrachan.network.request.GetUserRequest
import com.makentoshe.habrachan.network.response.GetUserResponse

class GetUserArena(
    val manager: GetUserManager<GetUserRequest>,
    cache: ArenaCache<GetUserRequest, GetUserResponse>
): SourceFirstArena<GetUserRequest, GetUserResponse>(cache) {
    override suspend fun internalSuspendFetch(key: GetUserRequest): Result<GetUserResponse> {
        return manager.user(key)
    }
}