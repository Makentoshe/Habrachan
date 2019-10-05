package com.makentoshe.habrachan.di

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.model.navigation.Navigator
import com.makentoshe.habrachan.ui.MainFlowFragmentUi
import com.makentoshe.habrachan.view.MainFlowFragment
import com.makentoshe.habrachan.view.MainFlowPresenter
import com.makentoshe.habrachan.viewmodel.MainFlowViewModel
import ru.terrakok.cicerone.Cicerone
import toothpick.config.Module
import toothpick.ktp.binding.bind

class MainFlowFragmentModule(fragment: MainFlowFragment) : Module() {

    init {
        val navigator = Navigator(fragment.requireActivity(), R.id.main_container, fragment.childFragmentManager)
        bind<Navigator>().toInstance(navigator)
        bind<MainFlowFragmentUi>().toInstance(MainFlowFragmentUi())
        bind<MainFlowViewModel>().toInstance(getViewModel(fragment))
        bind<MainFlowPresenter>().toInstance(MainFlowPresenter(getViewModel(fragment), navigator))
    }

    private fun getViewModel(fragment: MainFlowFragment): MainFlowViewModel {
        val factory = MainFlowViewModel.Factory(Cicerone.create())
        return ViewModelProviders.of(fragment, factory)[MainFlowViewModel::class.java]
    }
}