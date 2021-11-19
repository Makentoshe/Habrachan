package com.makentoshe.habrachan.application.common.arena.user.get

import com.makentoshe.habrachan.application.common.arena.Arena3
import com.makentoshe.habrachan.application.common.arena.ArenaCache3
import com.makentoshe.habrachan.functional.Either2
import com.makentoshe.habrachan.network.user.get.GetUserException
import com.makentoshe.habrachan.network.user.get.GetUserManager
import com.makentoshe.habrachan.network.user.get.GetUserRequest
import javax.inject.Inject

abstract class UsersArena internal constructor(
    private val manager: GetUserManager,
) : Arena3<GetUserArenaRequest, GetUserArenaResponse>() {

    override suspend fun internalSuspendFetch(key: GetUserArenaRequest): Either2<GetUserArenaResponse, GetUserException> {
        return manager.execute(GetUserRequest(key.login, key.parameters)).mapLeft {
            GetUserArenaResponse(key, UserFromArena(it.user.parameters))
        }
    }

    class Factory @Inject constructor(
        private val manager: GetUserManager,
        private val arenaStorage: ArenaCache3<in GetUserArenaRequest, GetUserArenaResponse>,
    ) {
        fun sourceFirstArena(): UsersArena {
            return SourceFirstUsersArena(manager, arenaStorage)
        }

        fun cacheFirstArena(): UsersArena {
            return CacheFirstUsersArena(manager, arenaStorage)
        }
    }
}

internal class SourceFirstUsersArena constructor(
    manager: GetUserManager,
    override val arenaStorage: ArenaCache3<in GetUserArenaRequest, GetUserArenaResponse>,
) : UsersArena(manager) {

    override suspend fun suspendFetch(key: GetUserArenaRequest) = sourceFirstSuspendFetch(key)
}

internal class CacheFirstUsersArena constructor(
    manager: GetUserManager,
    override val arenaStorage: ArenaCache3<in GetUserArenaRequest, GetUserArenaResponse>,
) : UsersArena(manager) {

    override suspend fun suspendFetch(key: GetUserArenaRequest) = cacheFirstSuspendFetch(key)
}
