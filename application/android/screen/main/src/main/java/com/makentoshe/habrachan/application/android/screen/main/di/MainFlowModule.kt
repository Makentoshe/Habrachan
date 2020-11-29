package com.makentoshe.habrachan.application.android.screen.main.di

import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.main.MainFlowFragment
import com.makentoshe.habrachan.application.android.screen.main.navigation.MainFlowNavigation
import com.makentoshe.habrachan.application.android.screen.main.navigation.MainFlowNavigationImpl
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import javax.inject.Qualifier

@Qualifier
annotation class MainFlowScope

class MainFlowModule(fragment: MainFlowFragment) : Module() {

    init {
        Toothpick.openScope(ApplicationScope::class)
        bind<MainFlowNavigation>().toInstance(MainFlowNavigationImpl(fragment.childFragmentManager))
    }
}