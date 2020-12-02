package com.makentoshe.habrachan.application.android.screen.main.di

import com.makentoshe.habrachan.application.android.screen.main.MainFlowFragment
import com.makentoshe.habrachan.application.android.screen.main.navigation.MainFlowNavigation
import com.makentoshe.habrachan.di.common.ApplicationScope
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import javax.inject.Qualifier

@Qualifier
annotation class MainFlowScope

class MainFlowModule(fragment: MainFlowFragment) : Module() {

    init {
        Toothpick.openScopes(ApplicationScope::class).inject(this)

        val navigation = MainFlowNavigation(fragment.childFragmentManager)
        bind<MainFlowNavigation>().toInstance(navigation)
    }
}