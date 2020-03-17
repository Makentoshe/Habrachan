package com.makentoshe.habrachan.di.common

import com.makentoshe.habrachan.common.navigation.Router
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import toothpick.config.Module
import toothpick.ktp.binding.bind

annotation class NavigationScope

class NavigationModule(cicerone: Cicerone<Router>): Module() {
    init {
        bind<Router>().toInstance(cicerone.router)
        bind<NavigatorHolder>().toInstance(cicerone.navigatorHolder)
    }
}