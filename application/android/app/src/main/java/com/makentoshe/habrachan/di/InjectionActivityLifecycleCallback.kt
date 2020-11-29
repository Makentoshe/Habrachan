package com.makentoshe.habrachan.di

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.makentoshe.habrachan.AppActivity
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import toothpick.Toothpick

class InjectionActivityLifecycleCallback(
    private vararg val callbacks: FragmentManager.FragmentLifecycleCallbacks
) : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (activity !is AppActivity) return

        Toothpick.openScopes(ApplicationScope::class).inject(activity)

        callbacks.forEach { activity.supportFragmentManager.registerFragmentLifecycleCallbacks(it, true) }
    }

    override fun onActivityStarted(activity: Activity?) = Unit
    override fun onActivityPaused(activity: Activity?) = Unit
    override fun onActivityResumed(activity: Activity?) = Unit
    override fun onActivityStopped(activity: Activity?) = Unit
    override fun onActivityDestroyed(activity: Activity?) = Unit
    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) = Unit
}