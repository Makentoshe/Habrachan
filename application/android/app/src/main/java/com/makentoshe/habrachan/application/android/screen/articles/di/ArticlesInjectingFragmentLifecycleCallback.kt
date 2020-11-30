package com.makentoshe.habrachan.application.android.screen.articles.di

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.makentoshe.habrachan.application.android.screen.articles.ArticlesFlowFragment
import com.makentoshe.habrachan.application.android.screen.articles.ArticlesFragment
import com.makentoshe.habrachan.application.android.screen.articles.ArticlesSearchFragment
import com.makentoshe.habrachan.di.common.ApplicationScope
import toothpick.Toothpick
import toothpick.smoothie.lifecycle.closeOnDestroy

class ArticlesInjectingFragmentLifecycleCallback : FragmentManager.FragmentLifecycleCallbacks() {

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) = when (f) {
        is ArticlesFlowFragment -> injectArticlesFlowFragment(f)
        is ArticlesFragment -> injectArticlesFragment(f)
        is ArticlesSearchFragment -> injectArticlesSearchFragment(f)
        else -> Unit
    }

    private fun injectArticlesFlowFragment(fragment: ArticlesFlowFragment) {
        val module = ArticlesFlowFragmentModule(fragment)
        val scope = Toothpick.openScopes(ApplicationScope::class.java, ArticlesFlowFragmentScope::class.java)
        scope.closeOnDestroy(fragment).installModules(module).inject(fragment)
    }

    private fun injectArticlesFragment(fragment: ArticlesFragment) {
        val module = ArticlesFragmentModule(fragment)
        val scope = Toothpick.openScopes(ArticlesFlowFragmentScope::class.java, ArticlesFragmentScope::class.java)
        scope.closeOnDestroy(fragment).installModules(module).inject(fragment)
    }

    private fun injectArticlesSearchFragment(fragment: ArticlesSearchFragment) {
        val scope = Toothpick.openScopes(ArticlesFlowFragmentScope::class.java, ArticlesSearchFragmentScope::class.java)
        scope.closeOnDestroy(fragment).inject(fragment)
    }

}
