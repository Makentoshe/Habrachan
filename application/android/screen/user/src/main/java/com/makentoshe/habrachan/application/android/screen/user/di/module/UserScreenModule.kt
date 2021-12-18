package com.makentoshe.habrachan.application.android.screen.user.di.module

import com.makentoshe.application.android.common.user.get.viewmodel.GetUserViewModel
import com.makentoshe.application.android.common.user.get.viewmodel.GetUserViewModelProvider
import com.makentoshe.habrachan.application.android.common.di.FragmentInjector
import com.makentoshe.habrachan.application.android.common.strings.BundledStringsProvider
import com.makentoshe.habrachan.application.android.common.user.me.viewmodel.MeUserViewModel
import com.makentoshe.habrachan.application.android.common.user.me.viewmodel.MeUserViewModelProvider
import com.makentoshe.habrachan.application.android.common.user.me.viewmodel.MeUserViewModelRequest
import com.makentoshe.habrachan.application.android.common.usersession.AndroidUserSessionProvider
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.user.UserFragment
import com.makentoshe.habrachan.application.android.screen.user.di.UserScope
import com.makentoshe.habrachan.application.common.arena.user.get.GetUserArena
import com.makentoshe.habrachan.application.common.arena.user.me.MeUserArena
import com.makentoshe.habrachan.functional.Option2
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class UserScreenModule(injectorScope: FragmentInjector.FragmentInjectorScope<UserFragment>) : Module() {

    private val stringsProvider by inject<BundledStringsProvider>()
    private val userSessionProvider by inject<AndroidUserSessionProvider>()
    private val meUserArena by inject<MeUserArena>()
    private val getUserArena by inject<GetUserArena>()

    init {
        Toothpick.openScopes(ApplicationScope::class, UserScope::class).inject(this)

        val meInitialOption = Option2.from(MeUserViewModelRequest())
        val meFactory = MeUserViewModel.Factory(stringsProvider, userSessionProvider, meUserArena, meInitialOption)
        bind<MeUserViewModel>().toInstance(MeUserViewModelProvider(meFactory).get(injectorScope.fragment))

        val getInitialOption = Option2.None
        val getFactory = GetUserViewModel.Factory(stringsProvider, userSessionProvider, getUserArena, getInitialOption)
        bind<GetUserViewModel>().toInstance(GetUserViewModelProvider(getFactory).get(injectorScope.fragment))
    }

}