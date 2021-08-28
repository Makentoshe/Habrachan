package com.makentoshe.habrachan.application.android.screen.comments.di.provider

import com.makentoshe.habrachan.application.android.common.arena.AvatarArenaCache
import com.makentoshe.habrachan.application.common.arena.content.ContentArena
import com.makentoshe.habrachan.network.GetContentManager
import javax.inject.Inject
import javax.inject.Provider

class CacheFirstAvatarArenaProvider @Inject constructor(
    private val manager: GetContentManager,
    private val arenaStorage: AvatarArenaCache,
) : Provider<ContentArena> {
    override fun get(): ContentArena {
        return ContentArena.Factory(manager, arenaStorage).cacheFirstArena()
    }
}