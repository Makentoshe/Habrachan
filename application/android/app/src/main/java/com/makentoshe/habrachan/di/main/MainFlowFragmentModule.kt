package com.makentoshe.habrachan.di.main

import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.navigation.main.MainFlowFragmentNavigation
import com.makentoshe.habrachan.view.main.MainFlowFragment
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