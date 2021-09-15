package com.makentoshe.habrachan.application.android.screen.articles.flow.di

import com.makentoshe.habrachan.application.android.common.di.FragmentInjector
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.articles.flow.ArticlesFlowFragment
import toothpick.Toothpick
import toothpick.smoothie.lifecycle.closeOnDestroy

class ArticlesFlowFragmentInjector: FragmentInjector<ArticlesFlowFragment>(
    fragmentClass = ArticlesFlowFragment::class,
    action = {
        val module = ArticlesFlowModule(fragment)
        val scope = Toothpick.openScopes(ApplicationScope::class, ArticlesFlowScope::class)
        scope.closeOnDestroy(fragment).installModules(module).inject(fragment)
    }
)
