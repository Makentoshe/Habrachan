package com.makentoshe.habrachan.application.android.screen.articles.di

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.articles.ArticlesFlowFragment
import com.makentoshe.habrachan.application.android.screen.articles.ArticlesFragment
import toothpick.Toothpick
import toothpick.smoothie.lifecycle.closeOnDestroy

class ArticlesInjectingFragmentLifecycleCallback : FragmentManager.FragmentLifecycleCallbacks() {

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) = when (f) {
        is ArticlesFlowFragment -> injectArticlesFlowFragment(f)
        is ArticlesFragment -> injectArticlesFragment(f)
        else -> Unit
    }

    private fun injectArticlesFlowFragment(fragment: ArticlesFlowFragment) {
        val module = ArticlesFlowModule(fragment)
        val scope = Toothpick.openScopes(ApplicationScope::class, ArticlesFlowScope::class)
        scope.closeOnDestroy(fragment).installModules(module).inject(fragment)
    }

    private fun injectArticlesFragment(fragment: ArticlesFragment) {
        val module = ArticlesModule(fragment)
        val scope = Toothpick.openScopes(ApplicationScope::class, ArticlesFlowScope::class, ArticlesScope::class, fragment)
        scope.closeOnDestroy(fragment).installModules(module).inject(fragment)
    }
}
