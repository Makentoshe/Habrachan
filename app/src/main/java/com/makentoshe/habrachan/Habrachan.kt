package com.makentoshe.habrachan

import android.app.Application
import com.makentoshe.habrachan.di.common.CacheModule
import com.makentoshe.habrachan.di.common.CacheScope
import com.makentoshe.habrachan.di.common.NavigationModule
import com.makentoshe.habrachan.di.common.NavigationScope
import ru.terrakok.cicerone.Cicerone
import toothpick.Toothpick

class Habrachan : Application() {

    private val cicerone = Cicerone.create()

    override fun onCreate() {
        super.onCreate()
        injectCacheDependencies()
        injectNavigationDependencies()
    }

    private fun injectCacheDependencies() {
        val module = CacheModule()
        Toothpick.openScopes(CacheScope::class.java).installModules(module)
    }

    private fun injectNavigationDependencies() {
        val module = NavigationModule(cicerone)
        Toothpick.openScope(NavigationScope::class.java).installModules(module)
    }

}
