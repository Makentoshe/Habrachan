package com.makentoshe.habrachan.application.android.screen.user.di.module

import com.makentoshe.habrachan.application.android.common.di.FragmentInjector
import com.makentoshe.habrachan.application.android.common.user.me.viewmodel.MeUserViewModel
import com.makentoshe.habrachan.application.android.common.user.me.viewmodel.MeUserViewModelProvider
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.user.UserFragment
import com.makentoshe.habrachan.application.android.screen.user.di.UserScope
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class SpecificUserModule(injectorScope: FragmentInjector.FragmentInjectorScope<UserFragment>) : Module() {

    private val meUserViewModelProvider by inject<MeUserViewModelProvider>()

    init {
        Toothpick.openScopes(ApplicationScope::class, UserScope::class).inject(this)

        bind<MeUserViewModel>().toInstance(meUserViewModelProvider.get(injectorScope.fragment))
    }

}