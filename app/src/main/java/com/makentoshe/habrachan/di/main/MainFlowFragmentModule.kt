package com.makentoshe.habrachan.di.main

import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.navigation.Navigator
import com.makentoshe.habrachan.common.navigation.Router
import com.makentoshe.habrachan.view.main.MainFlowFragment
import ru.terrakok.cicerone.Cicerone
import toothpick.config.Module
import toothpick.ktp.binding.bind

class MainFlowFragmentModule(fragment: MainFlowFragment) : Module() {

    init {
        val navigator = Navigator(fragment.requireActivity(), R.id.main_container, fragment.childFragmentManager)
        val (router, navigatorHolder) = Cicerone.create(Router()).let { it.router to it.navigatorHolder }
        bind<MainFlowFragment.Navigator>().toInstance(MainFlowFragment.Navigator(router, navigatorHolder, navigator))
    }
}