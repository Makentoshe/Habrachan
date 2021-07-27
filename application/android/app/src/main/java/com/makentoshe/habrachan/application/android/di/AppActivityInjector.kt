package com.makentoshe.habrachan.application.android.di

import com.makentoshe.habrachan.application.android.AppActivity
import com.makentoshe.habrachan.application.android.common.di.ActivityInjector
import com.makentoshe.habrachan.application.android.common.di.FragmentInjector
import toothpick.Toothpick

class AppActivityInjector(vararg fragmentInjectors: FragmentInjector<*>) : ActivityInjector<AppActivity>(
    activityClass = AppActivity::class,
    action = { savedInstanceState ->
        Toothpick.openScopes(ApplicationScope::class).inject(this)

        fragmentInjectors.forEach { fragmentInjector ->
            supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentInjector, true)
        }
    }
)