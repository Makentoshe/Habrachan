package com.makentoshe.habrachan.application.android.di

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.makentoshe.habrachan.application.android.AppActivity
import com.makentoshe.habrachan.application.android.screen.article.di.ArticleInjectingFragmentLifecycleCallback
import com.makentoshe.habrachan.application.android.screen.articles.di.ArticlesInjectingFragmentLifecycleCallback
import com.makentoshe.habrachan.application.android.screen.comments.di.ArticleCommentsInjectingFragmentLifecycleCallback
import com.makentoshe.habrachan.application.android.screen.content.di.ContentInjectingFragmentLifecycleCallback
import com.makentoshe.habrachan.application.android.screen.login.di.LoginInjectingFragmentLifecycleCallback
import com.makentoshe.habrachan.application.android.screen.main.di.MainFlowInjectingFragmentLifecycleCallback
import com.makentoshe.habrachan.application.android.screen.user.di.UserInjectingFragmentLifecycleCallback
import toothpick.Toothpick

class InjectionActivityLifecycleCallback() : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (activity !is AppActivity) return
        Toothpick.openScopes(ApplicationScope::class).inject(activity)

        val mainFlowCallback = MainFlowInjectingFragmentLifecycleCallback()
        activity.supportFragmentManager.registerFragmentLifecycleCallbacks(mainFlowCallback, true)

        val articlesCallback = ArticlesInjectingFragmentLifecycleCallback()
        activity.supportFragmentManager.registerFragmentLifecycleCallbacks(articlesCallback, true)

        val articleCallback = ArticleInjectingFragmentLifecycleCallback()
        activity.supportFragmentManager.registerFragmentLifecycleCallbacks(articleCallback, true)

        val articleCommentsCallback = ArticleCommentsInjectingFragmentLifecycleCallback()
        activity.supportFragmentManager.registerFragmentLifecycleCallbacks(articleCommentsCallback, true)

        val contentCallback = ContentInjectingFragmentLifecycleCallback()
        activity.supportFragmentManager.registerFragmentLifecycleCallbacks(contentCallback, true)

        val loginCallback = LoginInjectingFragmentLifecycleCallback()
        activity.supportFragmentManager.registerFragmentLifecycleCallbacks(loginCallback, true)

        val userCallback = UserInjectingFragmentLifecycleCallback()
        activity.supportFragmentManager.registerFragmentLifecycleCallbacks(userCallback, true)
    }


    override fun onActivityStarted(activity: Activity?) = Unit
    override fun onActivityPaused(activity: Activity?) = Unit
    override fun onActivityResumed(activity: Activity?) = Unit
    override fun onActivityStopped(activity: Activity?) = Unit
    override fun onActivityDestroyed(activity: Activity?) = Unit
    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) = Unit
}