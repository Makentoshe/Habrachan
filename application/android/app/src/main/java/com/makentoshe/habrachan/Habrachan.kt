package com.makentoshe.habrachan

import android.app.Application
import com.makentoshe.habrachan.application.android.di.ApplicationModule
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.main.di.MainFlowInjectingFragmentLifecycleCallback
import com.makentoshe.habrachan.di.InjectingFragmentLifecycleCallback
import com.makentoshe.habrachan.di.InjectionActivityLifecycleCallback
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import toothpick.configuration.Configuration

class Habrachan : Application() {

    private val disposables = CompositeDisposable()
    private val cicerone = Cicerone.create(Router())

    private val injectActivityLifecycleCallback = InjectionActivityLifecycleCallback(
        InjectingFragmentLifecycleCallback(), MainFlowInjectingFragmentLifecycleCallback()
    )

    override fun onCreate() {
        super.onCreate()
        Toothpick.setConfiguration(getToothpickConfiguration())

        val scopes = Toothpick.openScopes(ApplicationScope::class)
        scopes.installModules(ApplicationModule(applicationContext, cicerone))
        scopes.inject(this)

        registerActivityLifecycleCallbacks(injectActivityLifecycleCallback)
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
