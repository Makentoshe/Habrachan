package com.makentoshe.habrachan.application.android.screen.articles.di

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.articles.ArticlesFragment
import toothpick.Toothpick
import toothpick.smoothie.lifecycle.closeOnDestroy

class ArticlesInjectingFragmentLifecycleCallback : FragmentManager.FragmentLifecycleCallbacks() {

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) = when (f) {
        is ArticlesFragment -> injectArticlesFragment2(f)
        else -> Unit
    }

    private fun injectArticlesFragment2(fragment: ArticlesFragment) {
        val module = ArticlesFragmentModule(fragment)
        val scope = Toothpick.openScopes(ApplicationScope::class, ArticlesScope::class)
        scope.closeOnDestroy(fragment).installModules(module).inject(fragment)
    }
}
