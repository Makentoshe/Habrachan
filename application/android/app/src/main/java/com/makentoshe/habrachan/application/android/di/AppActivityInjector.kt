package com.makentoshe.habrachan.application.android.di

import com.makentoshe.habrachan.application.android.AppActivity
import com.makentoshe.habrachan.application.android.common.di.ActivityInjector
import com.makentoshe.habrachan.application.android.screen.article.di.ArticleInjectingFragmentLifecycleCallback
import com.makentoshe.habrachan.application.android.screen.articles.di.ArticlesInjectingFragmentLifecycleCallback
import com.makentoshe.habrachan.application.android.screen.comments.di.ArticleCommentsInjectingFragmentLifecycleCallback
import com.makentoshe.habrachan.application.android.screen.content.di.ContentInjectingFragmentLifecycleCallback
import com.makentoshe.habrachan.application.android.screen.login.di.LoginInjectingFragmentLifecycleCallback
import com.makentoshe.habrachan.application.android.screen.main.di.MainFlowInjectingFragmentLifecycleCallback
import com.makentoshe.habrachan.application.android.screen.user.di.UserInjectingFragmentLifecycleCallback
import toothpick.Toothpick

class AppActivityInjector : ActivityInjector<AppActivity>(AppActivity::class, { savedInstanceState ->
    Toothpick.openScopes(ApplicationScope::class).inject(this)

    val mainFlowCallback = MainFlowInjectingFragmentLifecycleCallback()
    supportFragmentManager.registerFragmentLifecycleCallbacks(mainFlowCallback, true)

    val articlesCallback = ArticlesInjectingFragmentLifecycleCallback()
    supportFragmentManager.registerFragmentLifecycleCallbacks(articlesCallback, true)

    val articleCallback = ArticleInjectingFragmentLifecycleCallback()
    supportFragmentManager.registerFragmentLifecycleCallbacks(articleCallback, true)

    val articleCommentsCallback = ArticleCommentsInjectingFragmentLifecycleCallback()
    supportFragmentManager.registerFragmentLifecycleCallbacks(articleCommentsCallback, true)

    val contentCallback = ContentInjectingFragmentLifecycleCallback()
    supportFragmentManager.registerFragmentLifecycleCallbacks(contentCallback, true)

    val loginCallback = LoginInjectingFragmentLifecycleCallback()
    supportFragmentManager.registerFragmentLifecycleCallbacks(loginCallback, true)

    val userCallback = UserInjectingFragmentLifecycleCallback()
    supportFragmentManager.registerFragmentLifecycleCallbacks(userCallback, true)
})