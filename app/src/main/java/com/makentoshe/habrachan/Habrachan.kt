package com.makentoshe.habrachan

import android.app.Application
import com.makentoshe.habrachan.common.navigation.Router
import com.makentoshe.habrachan.di.InjectingFragmentLifecycleCallback
import com.makentoshe.habrachan.di.InjectionActivityLifecycleCallback
import com.makentoshe.habrachan.di.common.*
import ru.terrakok.cicerone.Cicerone
import toothpick.Toothpick
import toothpick.configuration.Configuration

class Habrachan : Application() {

    private val cicerone = Cicerone.create(Router())

    private val injectFragmentLifecycleCallback = InjectingFragmentLifecycleCallback()
    private val injectActivityLifecycleCallback = InjectionActivityLifecycleCallback(injectFragmentLifecycleCallback)

    override fun onCreate() {
        super.onCreate()
        Toothpick.setConfiguration(getToothpickConfiguration())

        val scopes = Toothpick.openScopes(ApplicationScope::class.java)
        scopes.installModules(ApplicationModule(applicationContext), NavigationModule(cicerone))

        registerActivityLifecycleCallbacks(injectActivityLifecycleCallback)
    }

    private fun getToothpickConfiguration(): Configuration {
        return if (BuildConfig.DEBUG) {
            Configuration.forDevelopment().preventMultipleRootScopes()
        } else {
            Configuration.forProduction()
        }
    }
}
