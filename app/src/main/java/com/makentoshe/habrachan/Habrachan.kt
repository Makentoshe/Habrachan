package com.makentoshe.habrachan

import android.app.Application
import com.makentoshe.habrachan.di.InjectingFragmentLifecycleCallback
import com.makentoshe.habrachan.di.InjectionActivityLifecycleCallback
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
        injectRepositoryDependencies()
        val scopes = Toothpick.openScopes(RepositoryScope::class.java, ApplicationScope::class.java)
        scopes.installModules(ApplicationModule(applicationContext))

        val injectingFragmentLifecycleCallback = InjectingFragmentLifecycleCallback()
        registerActivityLifecycleCallbacks(InjectionActivityLifecycleCallback(injectingFragmentLifecycleCallback))
    }

    private fun getToothpickConfiguration(): Configuration {
        return if (BuildConfig.DEBUG) {
            Configuration.forDevelopment().preventMultipleRootScopes()
        } else {
            Configuration.forProduction()
        }
    }

    private fun injectCacheDependencies() {
        val module = CacheModule(applicationContext)
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

    private fun injectRepositoryDependencies() {
        val module = RepositoryModule(applicationContext)
        Toothpick.openScopes(NavigationScope::class.java, RepositoryScope::class.java).installModules(module)
    }

}
