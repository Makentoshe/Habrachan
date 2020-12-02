package com.makentoshe.habrachan

import android.app.Application
import com.makentoshe.habrachan.di.InjectingFragmentLifecycleCallback
import com.makentoshe.habrachan.di.InjectionActivityLifecycleCallback
import com.makentoshe.habrachan.di.common.ApplicationModule
import com.makentoshe.habrachan.di.common.ApplicationScope
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
        scopes.installModules(ApplicationModule(applicationContext, Cicerone.create(Router())))
        scopes.inject(this)

        val injectActivityLifecycleCallback = InjectionActivityLifecycleCallback(
            InjectingFragmentLifecycleCallback()
        )
        registerActivityLifecycleCallbacks(injectActivityLifecycleCallback)

        //        Thread.setDefaultUncaughtExceptionHandler { paramThread, paramThrowable ->
        //            println(paramThrowable.printStackTrace())
        //            throw paramThrowable
        //        }
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
