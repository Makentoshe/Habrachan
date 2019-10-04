package com.makentoshe.habrachan.di

import com.makentoshe.habrachan.AppActivity
import com.makentoshe.habrachan.R
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import toothpick.config.Module
import toothpick.ktp.binding.bind

class AppActivityModule(appActivity: AppActivity) : Module() {

    init {
        bind<Navigator>().toInstance(SupportAppNavigator(appActivity, R.id.main_container))
    }
}