package com.makentoshe.habrachan.application.android.screen.user.di.module

import android.content.Context
import android.content.SharedPreferences
import com.makentoshe.habrachan.application.android.common.di.module.BaseNetworkModule
import com.makentoshe.habrachan.application.android.common.user.me.arena.MeUserArenaStorage
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.common.arena.user.me.MeUserArena
import com.makentoshe.habrachan.network.me.mobile.MeUserManager
import toothpick.Toothpick
import toothpick.ktp.binding.bind

class MeUserNetworkModule(context: Context) : BaseNetworkModule() {

    init {
        Toothpick.openScopes(ApplicationScope::class).inject(this)

        val meUserArena = MeUserArena(MeUserManager(ktorHttpClient), MeUserArenaStorage(sharedPreferences(context)))
        bind<MeUserArena>().toInstance(meUserArena)
    }

    private fun sharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("MeUserSharedPreferences", Context.MODE_PRIVATE)
    }
}
