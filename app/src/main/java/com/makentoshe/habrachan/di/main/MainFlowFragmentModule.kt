package com.makentoshe.habrachan.di.main

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.navigation.Navigator
import com.makentoshe.habrachan.model.main.MainFlowBroadcastReceiver
import com.makentoshe.habrachan.ui.main.MainFlowFragmentUi
import com.makentoshe.habrachan.view.main.MainFlowFragment
import com.makentoshe.habrachan.view.main.MainFlowPresenter
import com.makentoshe.habrachan.viewmodel.main.MainFlowViewModel
import ru.terrakok.cicerone.Cicerone
import toothpick.config.Module
import toothpick.ktp.binding.bind

class MainFlowFragmentModule(fragment: MainFlowFragment) : Module() {

    init {
        val navigator = Navigator(fragment.requireActivity(), R.id.main_container, fragment.childFragmentManager)
        bind<Navigator>().toInstance(navigator)
        bind<MainFlowFragmentUi>().toInstance(MainFlowFragmentUi())
        bind<MainFlowViewModel>().toInstance(getViewModel(fragment))
        bind<MainFlowPresenter>().toInstance(
            MainFlowPresenter(
                getViewModel(fragment),
                navigator
            )
        )
        val broadcastReceiver = MainFlowBroadcastReceiver.registerReceiver(fragment.requireActivity())
        bind<MainFlowBroadcastReceiver>().toInstance(broadcastReceiver)
    }

    private fun getViewModel(fragment: MainFlowFragment): MainFlowViewModel {
        val factory = MainFlowViewModel.Factory(Cicerone.create(), page = 0)
        return ViewModelProviders.of(fragment, factory)[MainFlowViewModel::class.java]
    }
}