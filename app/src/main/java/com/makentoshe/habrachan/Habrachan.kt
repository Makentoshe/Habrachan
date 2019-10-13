package com.makentoshe.habrachan

import android.app.Application
import com.makentoshe.habrachan.di.common.*
import ru.terrakok.cicerone.Cicerone
import toothpick.Toothpick
import toothpick.configuration.Configuration

class Habrachan : Application() {

    private val cicerone = Cicerone.create()

    override fun onCreate() {
        super.onCreate()
        Toothpick.setConfiguration(getToothpickConfiguration())
        injectCacheDependencies()
        injectNavigationDependencies()
        injectNetworkDependencies()
    }

    private fun getToothpickConfiguration(): Configuration {
        return if (BuildConfig.DEBUG) {
            // allowing multiple root scopes
            Configuration.forDevelopment()
        } else {
            Configuration.forProduction()
        }
    }

    private fun injectCacheDependencies() {
        val module = CacheModule()
        Toothpick.openScope(CacheScope::class.java).installModules(module)
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
