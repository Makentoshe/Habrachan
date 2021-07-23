package com.makentoshe.habrachan.application.android.screen.article.di

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.article.ArticleFragment
import toothpick.Toothpick
import toothpick.smoothie.lifecycle.closeOnDestroy

class ArticleInjectingFragmentLifecycleCallback : FragmentManager.FragmentLifecycleCallbacks() {

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) = when (f) {
        is ArticleFragment -> injectArticleFragment(f)
        else -> Unit
    }

    private fun injectArticleFragment(fragment: ArticleFragment) {
        val module = ArticleModule(fragment) // TODO separate module for all Articles and for concrete article
        val scope = Toothpick.openScopes(ApplicationScope::class, ArticleScope::class, fragment.arguments.articleId)
        scope.installModules(module).closeOnDestroy(fragment).inject(fragment)
    }
}
