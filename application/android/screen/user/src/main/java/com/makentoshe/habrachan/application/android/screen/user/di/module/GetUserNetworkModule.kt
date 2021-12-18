package com.makentoshe.habrachan.application.android.screen.user.di.module

import com.makentoshe.application.android.common.user.get.arena.GetUserArenaStorage
import com.makentoshe.habrachan.application.android.common.di.module.BaseNetworkModule
import com.makentoshe.habrachan.application.android.database.cache.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.common.arena.user.get.GetUserArena
import com.makentoshe.habrachan.network.user.get.GetUserManager
import toothpick.Toothpick
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class GetUserNetworkModule : BaseNetworkModule() {

    private val cacheDatabase by inject<AndroidCacheDatabase>()

    init {
        Toothpick.openScopes(ApplicationScope::class).inject(this)

        val getUserArena = GetUserArena(GetUserManager(ktorHttpClient), GetUserArenaStorage(cacheDatabase))
        bind<GetUserArena>().toInstance(getUserArena)
    }

}