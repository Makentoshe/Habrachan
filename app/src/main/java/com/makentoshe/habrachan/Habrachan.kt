package com.makentoshe.habrachan

import android.app.Application
import com.makentoshe.habrachan.di.ApplicationModule
import com.makentoshe.habrachan.di.ApplicationScope
import com.makentoshe.habrachan.di.common.*
import okhttp3.OkHttpClient
import ru.terrakok.cicerone.Cicerone
import toothpick.Toothpick
import toothpick.configuration.Configuration
import toothpick.ktp.delegate.inject

class Habrachan : Application() {

    private val cicerone = Cicerone.create()

    private val client by inject<OkHttpClient>()

    override fun onCreate() {
        super.onCreate()
        Toothpick.setConfiguration(getToothpickConfiguration())
        injectCacheDependencies()
        injectNetworkDependencies()
        injectNavigationDependencies()
        injectRepositoryDependencies()
        val scopes = Toothpick.openScopes(RepositoryScope::class.java, ApplicationScope::class.java)
        scopes.installModules(ApplicationModule(applicationContext))
    }

    private fun getToothpickConfiguration(): Configuration {
        return if (BuildConfig.DEBUG) {
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
        val scope = Toothpick.openScopes(CacheScope::class.java, NetworkScope::class.java)
        scope.installModules(module)
        scope.inject(this)
    }

    private fun injectNavigationDependencies() {
        val module = NavigationModule(cicerone)
        Toothpick.openScopes(NetworkScope::class.java, NavigationScope::class.java).installModules(module)
    }

    private fun injectRepositoryDependencies() {
        val module = RepositoryModule(applicationContext, client)
        val scope = Toothpick.openScopes(NavigationScope::class.java, RepositoryScope::class.java)
        scope.installModules(module)
    }

}
