package com.makentoshe.habrachan.application.android.screen.main.di

import com.makentoshe.habrachan.application.android.screen.main.MainFlowFragment
import com.makentoshe.habrachan.application.android.screen.main.navigation.MainFlowFragmentNavigation
import com.makentoshe.habrachan.di.common.ApplicationScope
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind

class MainFlowFragmentModule(fragment: MainFlowFragment) : Module() {

    init {
        Toothpick.openScopes(ApplicationScope::class.java).inject(this)

        val fragmentNavigation = MainFlowFragmentNavigation(fragment.childFragmentManager)
        bind<MainFlowFragmentNavigation>().toInstance(fragmentNavigation)
    }
}