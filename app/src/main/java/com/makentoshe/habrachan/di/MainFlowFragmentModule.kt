package com.makentoshe.habrachan.di

import com.makentoshe.habrachan.ui.MainFlowFragmentUi
import toothpick.config.Module
import toothpick.ktp.binding.bind

class MainFlowFragmentModule : Module() {

    init {
        bind<MainFlowFragmentUi>().toInstance(MainFlowFragmentUi())
    }
}