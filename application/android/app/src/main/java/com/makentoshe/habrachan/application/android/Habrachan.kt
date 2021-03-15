package com.makentoshe.habrachan.application.android

import android.app.Application
import com.makentoshe.habrachan.BuildConfig
import com.makentoshe.habrachan.application.android.di.ApplicationModule
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.di.InjectionActivityLifecycleCallback
import com.makentoshe.habrachan.application.android.navigation.StackRouter
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import toothpick.configuration.Configuration

class Habrachan : Application() {

    private val disposables = CompositeDisposable()

    override fun onCreate() {
        super.onCreate()
        Toothpick.setConfiguration(getToothpickConfiguration())

        val scopes = Toothpick.openScopes(ApplicationScope::class)
        scopes.installModules(ApplicationModule(applicationContext, Cicerone.create(StackRouter())))
        scopes.inject(this)

        val injectActivityLifecycleCallback = InjectionActivityLifecycleCallback()
        registerActivityLifecycleCallbacks(injectActivityLifecycleCallback)

        Thread.setDefaultUncaughtExceptionHandler { paramThread, paramThrowable ->
            println(paramThrowable.printStackTrace())
            throw paramThrowable
        }
    }

    // Using composite disposable is just formality to avoid lint warnings
    override fun onTerminate() {
        super.onTerminate()
        disposables.clear()
    }

    private fun getToothpickConfiguration(): Configuration {
        return if (BuildConfig.DEBUG) {
            Configuration.forDevelopment().preventMultipleRootScopes()
        } else {
            Configuration.forProduction()
        }
    }
}
