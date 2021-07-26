package com.makentoshe.habrachan.application.android.screen.articles.di

import com.makentoshe.habrachan.application.android.common.di.FragmentInjector
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.articles.ArticlesFragment
import toothpick.Toothpick
import toothpick.smoothie.lifecycle.closeOnDestroy

class ArticlesFragmentInjector: FragmentInjector<ArticlesFragment>(
    fragmentClass = ArticlesFragment::class,
    action = {
        val module = ArticlesModule(fragment)
        val scope =
            Toothpick.openScopes(ApplicationScope::class, ArticlesFlowScope::class, ArticlesScope::class, fragment)
        scope.closeOnDestroy(fragment).installModules(module).inject(fragment)
    }
)