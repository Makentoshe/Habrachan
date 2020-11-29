package com.makentoshe.habrachan.di.common

import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import toothpick.config.Module
import toothpick.ktp.binding.bind

class NavigationModule(cicerone: Cicerone<Router>) : Module() {
    init {
        bind<Router>().toInstance(cicerone.router)
        bind<NavigatorHolder>().toInstance(cicerone.navigatorHolder)
    }
}