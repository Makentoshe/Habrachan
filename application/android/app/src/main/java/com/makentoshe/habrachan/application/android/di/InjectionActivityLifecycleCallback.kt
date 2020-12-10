package com.makentoshe.habrachan.application.android.di

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.makentoshe.habrachan.AppActivity
import com.makentoshe.habrachan.application.android.screen.article.di.ArticleInjectingFragmentLifecycleCallback
import com.makentoshe.habrachan.application.android.screen.articles.di.ArticlesInjectingFragmentLifecycleCallback
import com.makentoshe.habrachan.application.android.screen.main.di.MainFlowInjectingFragmentLifecycleCallback
import toothpick.Toothpick

class InjectionActivityLifecycleCallback(
    private val injectingFragmentLifecycleCallback: InjectingFragmentLifecycleCallback
) : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (activity !is AppActivity) return
        Toothpick.openScopes(ApplicationScope::class).inject(activity)

        activity.supportFragmentManager.registerFragmentLifecycleCallbacks(
            injectingFragmentLifecycleCallback,
            true
        )

        val mainFlowCallback = MainFlowInjectingFragmentLifecycleCallback()
        activity.supportFragmentManager.registerFragmentLifecycleCallbacks(mainFlowCallback, true)

        val articlesCallback = ArticlesInjectingFragmentLifecycleCallback()
        activity.supportFragmentManager.registerFragmentLifecycleCallbacks(articlesCallback, true)

        val articleCallback = ArticleInjectingFragmentLifecycleCallback()
        activity.supportFragmentManager.registerFragmentLifecycleCallbacks(articleCallback, true)
    }


    override fun onActivityStarted(activity: Activity?) = Unit
    override fun onActivityPaused(activity: Activity?) = Unit
    override fun onActivityResumed(activity: Activity?) = Unit
    override fun onActivityStopped(activity: Activity?) = Unit
    override fun onActivityDestroyed(activity: Activity?) = Unit
    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) = Unit
}