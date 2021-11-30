package com.makentoshe.habrachan.application.android.screen.user.di.module

import android.content.Context
import android.content.SharedPreferences
import com.makentoshe.habrachan.application.android.common.di.module.BaseNetworkModule
import com.makentoshe.habrachan.application.android.common.user.me.arena.MeUserArenaStorage
import com.makentoshe.habrachan.application.common.arena.ArenaCache3
import com.makentoshe.habrachan.application.common.arena.user.me.MeUserArena
import com.makentoshe.habrachan.application.common.arena.user.me.MeUserArenaRequest
import com.makentoshe.habrachan.application.common.arena.user.me.MeUserArenaResponse
import com.makentoshe.habrachan.network.me.mobile.MeUserManager
import toothpick.ktp.binding.bind

class MeUserNetworkModule(context: Context): BaseNetworkModule() {

    init {
        bind<SharedPreferences>().toInstance(sharedPreferences(context))
        bind<MeUserManager>().toInstance(MeUserManager(ktorHttpClient))

        bind<ArenaCache3<MeUserArenaRequest, MeUserArenaResponse>>().toClass<MeUserArenaStorage>().singleton()
        bind<MeUserArena>().toClass<MeUserArena>().singleton()
    }

    private fun sharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("MeUserSharedPreferences", Context.MODE_PRIVATE)
    }
}
