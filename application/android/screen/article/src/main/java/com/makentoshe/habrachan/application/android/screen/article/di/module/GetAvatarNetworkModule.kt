package com.makentoshe.habrachan.application.android.screen.article.di.module

import com.makentoshe.habrachan.application.android.common.avatar.arena.GetAvatarArenaStorage
import com.makentoshe.habrachan.application.android.common.di.module.BaseNetworkModule
import com.makentoshe.habrachan.application.android.database.cache.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.common.arena.content.GetContentArena
import com.makentoshe.habrachan.network.content.get.DefaultGetContentManager
import toothpick.Toothpick
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject
import java.io.File

class GetAvatarNetworkModule : BaseNetworkModule() {

    private val cacheDatabase by inject<AndroidCacheDatabase>()
    private val cacheFilesystemRoot by inject<File>()

    init {
        Toothpick.openScopes(ApplicationScope::class).inject(this)

        val arenaStorage = GetAvatarArenaStorage(cacheDatabase, cacheFilesystemRoot)
        val getAvatarArena = GetContentArena(DefaultGetContentManager(ktorHttpClient), arenaStorage)
        bind<GetContentArena>().toInstance(getAvatarArena)

    }

}