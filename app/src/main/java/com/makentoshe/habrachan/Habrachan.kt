package com.makentoshe.habrachan

import android.app.Application
import com.makentoshe.habrachan.di.common.*
import ru.terrakok.cicerone.Cicerone
import toothpick.Toothpick

class Habrachan : Application() {

    private val cicerone = Cicerone.create()

    override fun onCreate() {
        super.onCreate()
        injectCacheDependencies()
        injectNavigationDependencies()
        injectNetworkDependencies()
    }

    private fun injectCacheDependencies() {
        val module = CacheModule()
        Toothpick.openScopes(CacheScope::class.java).installModules(module)
    }

    private fun injectNavigationDependencies() {
        val module = NavigationModule(cicerone)
        Toothpick.openScope(NavigationScope::class.java).installModules(module)
    }

    private fun injectNetworkDependencies() {
        val module = NetworkModule(applicationContext)
        Toothpick.openScope(NetworkScope::class.java).installModules(module)
    }

}
