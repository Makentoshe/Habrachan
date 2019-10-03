package com.makentoshe.habrachan.di.main

import com.makentoshe.habrachan.ui.main.MainFlowFragmentUi
import toothpick.config.Module
import toothpick.ktp.binding.bind

class MainFlowFragmentModule: Module() {
    init {
        bind<MainFlowFragmentUi>().toInstance(MainFlowFragmentUi())
    }
}