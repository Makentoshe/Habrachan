package com.makentoshe.habrachan.di

import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import toothpick.config.Module
import toothpick.ktp.binding.bind

class ApplicationModule(cicerone: Cicerone<Router>): Module() {
    init {
        initNavigationBindings(cicerone)
    }

    private fun initNavigationBindings(cicerone: Cicerone<Router>) {
        bind<Router>().toInstance(cicerone.router)
        bind<NavigatorHolder>().toInstance(cicerone.navigatorHolder)
    }
}