package com.makentoshe.habrachan.application.common.arena.user.me

import com.makentoshe.habrachan.application.common.arena.Arena3
import com.makentoshe.habrachan.application.common.arena.ArenaCache3
import com.makentoshe.habrachan.functional.Either2
import com.makentoshe.habrachan.network.me.mobile.MeUserManager
import com.makentoshe.habrachan.network.me.mobile.MeUserRequest
import javax.inject.Inject

abstract class MeUserArena internal constructor(
    private val manager: MeUserManager,
) : Arena3<MeUserArenaRequest, MeUserArenaResponse>() {

    override suspend fun internalSuspendFetch(key: MeUserArenaRequest): Either2<MeUserArenaResponse, Throwable> {
        val managerRequest = MeUserRequest(key.parameters)
        val managerResponse = manager.execute(managerRequest)
        return managerResponse.mapLeft { MeUserArenaResponse(key, MeUserFromArena(it.me.parameters)) }
    }

    class Factory @Inject constructor(
        private val manager: MeUserManager,
        private val arenaStorage: ArenaCache3<in MeUserArenaRequest, MeUserArenaResponse>,
    ) {
        fun sourceFirstArena(): MeUserArena {
            return SourceFirstUsersArena(manager, arenaStorage)
        }

        fun cacheFirstArena(): MeUserArena {
            return CacheFirstUsersArena(manager, arenaStorage)
        }
    }
}

internal class SourceFirstUsersArena constructor(
    manager: MeUserManager,
    override val arenaStorage: ArenaCache3<in MeUserArenaRequest, MeUserArenaResponse>,
) : MeUserArena(manager) {

    override suspend fun suspendFetch(key: MeUserArenaRequest) = sourceFirstSuspendFetch(key)
}

internal class CacheFirstUsersArena constructor(
    manager: MeUserManager,
    override val arenaStorage: ArenaCache3<in MeUserArenaRequest, MeUserArenaResponse>
) : MeUserArena(manager) {

    override suspend fun suspendFetch(key: MeUserArenaRequest) = cacheFirstSuspendFetch(key)
}
