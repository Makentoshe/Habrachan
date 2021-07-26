package com.makentoshe.habrachan.application.android.di

import com.makentoshe.habrachan.application.android.AppActivity
import com.makentoshe.habrachan.application.android.common.di.ActivityInjector
import com.makentoshe.habrachan.application.android.common.di.FragmentInjector
import com.makentoshe.habrachan.application.android.screen.comments.di.ArticleCommentsInjectingFragmentLifecycleCallback
import com.makentoshe.habrachan.application.android.screen.login.di.LoginInjectingFragmentLifecycleCallback
import com.makentoshe.habrachan.application.android.screen.user.di.UserInjectingFragmentLifecycleCallback
import toothpick.Toothpick

class AppActivityInjector(vararg fragmentInjectors: FragmentInjector<*>) : ActivityInjector<AppActivity>(
    activityClass = AppActivity::class,
    action = { savedInstanceState ->
        Toothpick.openScopes(ApplicationScope::class).inject(this)

        fragmentInjectors.forEach { fragmentInjector ->
            supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentInjector, true)
        }

    val articleCommentsCallback = ArticleCommentsInjectingFragmentLifecycleCallback()
    supportFragmentManager.registerFragmentLifecycleCallbacks(articleCommentsCallback, true)

    val loginCallback = LoginInjectingFragmentLifecycleCallback()
    supportFragmentManager.registerFragmentLifecycleCallbacks(loginCallback, true)

    val userCallback = UserInjectingFragmentLifecycleCallback()
    supportFragmentManager.registerFragmentLifecycleCallbacks(userCallback, true)
})