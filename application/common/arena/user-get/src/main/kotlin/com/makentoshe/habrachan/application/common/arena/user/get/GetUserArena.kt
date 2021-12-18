package com.makentoshe.habrachan.application.common.arena.user.get

import com.makentoshe.habrachan.application.common.arena.ArenaCache3
import com.makentoshe.habrachan.application.common.arena.FlowArena
import com.makentoshe.habrachan.functional.Either2
import com.makentoshe.habrachan.network.user.get.GetUserManager
import com.makentoshe.habrachan.network.user.get.GetUserRequest
import javax.inject.Inject

class GetUserArena @Inject constructor(
    private val manager: GetUserManager,
    override val arenaStorage: ArenaCache3<in GetUserArenaRequest, GetUserArenaResponse>,
) : FlowArena<GetUserArenaRequest, GetUserArenaResponse>() {
    override suspend fun suspendSourceFetch(key: GetUserArenaRequest): Either2<GetUserArenaResponse, Throwable> {
        val managerRequest = GetUserRequest(key.login, key.parameters)
        val managerResponse = manager.execute(managerRequest)
        return managerResponse.mapLeft { GetUserArenaResponse(key, UserFromArena(it.user.parameters)) }
    }
}
