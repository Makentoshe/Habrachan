package com.makentoshe.habrachan.di

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.makentoshe.habrachan.AppActivity
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.navigation.Navigator
import com.makentoshe.habrachan.common.navigation.lifecycle.NavigatorHolderLifecycleObserver
import com.makentoshe.habrachan.di.common.ApplicationScope
import ru.terrakok.cicerone.NavigatorHolder
import toothpick.Toothpick
import toothpick.ktp.delegate.inject

class InjectionActivityLifecycleCallback(
    private val injectingFragmentLifecycleCallback: InjectingFragmentLifecycleCallback
) : Application.ActivityLifecycleCallbacks {

    private val navigatorHolder by inject<NavigatorHolder>()

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (activity !is AppActivity) return
        Toothpick.openScopes(ApplicationScope::class.java).apply {
            inject(activity)
            inject(this@InjectionActivityLifecycleCallback)
        }

        activity.supportFragmentManager.registerFragmentLifecycleCallbacks(injectingFragmentLifecycleCallback, true)

        bindNavigatorToActivity(activity, Navigator(activity, R.id.main_container))
    }

    private fun bindNavigatorToActivity(activity: AppActivity, navigator: Navigator) {
        activity.lifecycle.addObserver(
            NavigatorHolderLifecycleObserver(
                navigatorHolder,
                navigator
            )
        )
    }

    override fun onActivityStarted(activity: Activity?) = Unit
    override fun onActivityPaused(activity: Activity?) = Unit
    override fun onActivityResumed(activity: Activity?) = Unit
    override fun onActivityStopped(activity: Activity?) = Unit
    override fun onActivityDestroyed(activity: Activity?) = Unit
    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) = Unit
}