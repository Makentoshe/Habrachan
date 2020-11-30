package com.makentoshe.habrachan

import android.app.Application
import android.content.IntentFilter
import com.makentoshe.habrachan.common.broadcast.LogoutBroadcastReceiver
import com.makentoshe.habrachan.common.database.session.SessionDatabase
import com.makentoshe.habrachan.di.InjectingFragmentLifecycleCallback
import com.makentoshe.habrachan.di.InjectionActivityLifecycleCallback
import com.makentoshe.habrachan.di.common.ApplicationModule
import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.di.common.NavigationModule
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import toothpick.configuration.Configuration
import toothpick.ktp.delegate.inject

class Habrachan : Application() {

    private val cicerone = Cicerone.create(Router())

    private val injectFragmentLifecycleCallback = InjectingFragmentLifecycleCallback()
    private val injectActivityLifecycleCallback = InjectionActivityLifecycleCallback(injectFragmentLifecycleCallback)

    private val logoutBroadcastReceiver = LogoutBroadcastReceiver()
    private val disposables = CompositeDisposable()

    private val sessionDatabase by inject<SessionDatabase>()

    override fun onCreate() {
        super.onCreate()
        Toothpick.setConfiguration(getToothpickConfiguration())

        val scopes = Toothpick.openScopes(ApplicationScope::class.java)
        scopes.installModules(ApplicationModule(applicationContext), NavigationModule(cicerone))
        scopes.inject(this)

        registerActivityLifecycleCallbacks(injectActivityLifecycleCallback)

        logoutBroadcastReceiver.observable.subscribe {
            sessionDatabase.me().clear()
            // remove token from session
            sessionDatabase.session().insert(sessionDatabase.session().get().copy(tokenKey = ""))
        }.let(disposables::add)

        registerReceiver(logoutBroadcastReceiver, IntentFilter(LogoutBroadcastReceiver.ACTION))

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
