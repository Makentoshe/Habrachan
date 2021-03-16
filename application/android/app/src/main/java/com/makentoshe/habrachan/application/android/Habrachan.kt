package com.makentoshe.habrachan.application.android

import android.app.Application
import com.makentoshe.habrachan.BuildConfig
import com.makentoshe.habrachan.application.android.di.ApplicationModule
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.di.InjectionActivityLifecycleCallback
import com.makentoshe.habrachan.application.android.di.NetworkModule
import com.makentoshe.habrachan.application.android.navigation.StackRouter
import ru.terrakok.cicerone.Cicerone
import toothpick.Toothpick
import toothpick.configuration.Configuration

class Habrachan : Application() {

    override fun onCreate() {
        super.onCreate()
        Toothpick.setConfiguration(getToothpickConfiguration())

        val applicationModule = ApplicationModule(applicationContext, Cicerone.create(StackRouter()))
        val networkModule = NetworkModule(applicationContext)

        val scopes = Toothpick.openScopes(ApplicationScope::class)
        scopes.installModules(applicationModule, networkModule).inject(this)

        val injectActivityLifecycleCallback = InjectionActivityLifecycleCallback()
        registerActivityLifecycleCallbacks(injectActivityLifecycleCallback)

        Thread.setDefaultUncaughtExceptionHandler { paramThread, paramThrowable ->
            println(paramThrowable.printStackTrace())
            throw paramThrowable
        }
    }

    private fun getToothpickConfiguration(): Configuration {
        return if (BuildConfig.DEBUG) {
            Configuration.forDevelopment().preventMultipleRootScopes()
        } else {
            Configuration.forProduction()
        }
    }
}
