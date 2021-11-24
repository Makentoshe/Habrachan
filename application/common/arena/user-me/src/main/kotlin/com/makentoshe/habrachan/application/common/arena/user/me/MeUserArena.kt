package com.makentoshe.habrachan.application.common.arena.user.me

import com.makentoshe.habrachan.application.common.arena.ArenaCache3
import com.makentoshe.habrachan.application.common.arena.FlowArena
import com.makentoshe.habrachan.functional.Either2
import com.makentoshe.habrachan.network.me.mobile.MeUserManager
import com.makentoshe.habrachan.network.me.mobile.MeUserRequest

class MeUserArena internal constructor(
    private val manager: MeUserManager,
    override val arenaStorage: ArenaCache3<in MeUserArenaRequest, MeUserArenaResponse>,
) : FlowArena<MeUserArenaRequest, MeUserArenaResponse>() {

    override suspend fun suspendSourceFetch(key: MeUserArenaRequest): Either2<MeUserArenaResponse, Throwable> {
        val managerRequest = MeUserRequest(key.parameters)
        val managerResponse = manager.execute(managerRequest)
        return managerResponse.mapLeft { MeUserArenaResponse(key, MeUserFromArena(it.me.parameters)) }
    }
}
