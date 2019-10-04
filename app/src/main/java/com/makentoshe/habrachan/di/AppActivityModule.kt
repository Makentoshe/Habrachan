package com.makentoshe.habrachan.di

import com.makentoshe.habrachan.AppActivity
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.model.navigation.Navigator
import toothpick.config.Module
import toothpick.ktp.binding.bind

class AppActivityModule(appActivity: AppActivity) : Module() {

    init {
        bind<Navigator>().toInstance(Navigator(appActivity, R.id.main_container))
    }
}