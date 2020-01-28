package com.makentoshe.habrachan.di.main

import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.navigation.Navigator
import com.makentoshe.habrachan.model.main.MainFlowBroadcastReceiver
import com.makentoshe.habrachan.ui.main.MainFlowFragmentUi
import com.makentoshe.habrachan.view.main.MainFlowFragment
import ru.terrakok.cicerone.Cicerone
import toothpick.config.Module
import toothpick.ktp.binding.bind

class MainFlowFragmentModule(fragment: MainFlowFragment) : Module() {

    init {
        val navigator = Navigator(fragment.requireActivity(), R.id.main_container, fragment.childFragmentManager)
        bind<Navigator>().toInstance(navigator)
        bind<MainFlowFragmentUi>().toInstance(MainFlowFragmentUi())
        val broadcastReceiver = MainFlowBroadcastReceiver.registerReceiver(fragment.requireActivity())
        bind<MainFlowBroadcastReceiver>().toInstance(broadcastReceiver)

        val (router, navigatorHolder) = Cicerone.create().let { it.router to it.navigatorHolder }
        bind<MainFlowFragment.Navigator>().toInstance(MainFlowFragment.Navigator(router, navigatorHolder, navigator))
    }
}