package com.makentoshe.habrachan

import android.app.Application
import com.makentoshe.habrachan.di.ApplicationModule
import com.makentoshe.habrachan.di.ApplicationScope
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
        injectNetworkDependencies()
        injectNavigationDependencies()
        val scopes = Toothpick.openScopes(NavigationScope::class.java, ApplicationScope::class.java)
        scopes.installModules(ApplicationModule(applicationContext))
    }

    private fun getToothpickConfiguration(): Configuration {
        return if (BuildConfig.DEBUG) {
            // allowing multiple root scopes
            Configuration.forDevelopment().preventMultipleRootScopes()
        } else {
            Configuration.forProduction()
        }
    }

    private fun injectCacheDependencies() {
        val module = CacheModule()
        Toothpick.openScope(CacheScope::class.java).installModules(module)
    }

    private fun injectNetworkDependencies() {
        val module = NetworkModule(applicationContext)
        Toothpick.openScopes(CacheScope::class.java, NetworkScope::class.java).installModules(module)
    }

    private fun injectNavigationDependencies() {
        val module = NavigationModule(cicerone)
        Toothpick.openScopes(NetworkScope::class.java, NavigationScope::class.java).installModules(module)
    }


}
