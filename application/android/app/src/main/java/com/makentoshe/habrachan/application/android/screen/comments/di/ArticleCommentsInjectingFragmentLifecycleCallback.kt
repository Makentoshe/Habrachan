package com.makentoshe.habrachan.application.android.screen.comments.di

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.comments.ArticleCommentsFragment
import com.makentoshe.habrachan.application.android.screen.comments.DiscussionCommentsFragment
import toothpick.Toothpick
import toothpick.smoothie.lifecycle.closeOnDestroy

class ArticleCommentsInjectingFragmentLifecycleCallback : FragmentManager.FragmentLifecycleCallbacks() {

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) = when (f) {
        is ArticleCommentsFragment -> injectArticleCommentsFragment(f)
        is DiscussionCommentsFragment -> injectDiscussionCommentsFragment(f)
        else -> Unit
    }

    private fun injectArticleCommentsFragment(fragment: ArticleCommentsFragment) {
        val module = ArticleCommentsModule(fragment)
        val scope = Toothpick.openScopes(ApplicationScope::class, ArticleCommentsScope::class)
        scope.closeOnDestroy(fragment).installModules(module).inject(fragment)
    }

    private fun injectDiscussionCommentsFragment(fragment: DiscussionCommentsFragment) {
        val module = DiscussionCommentsModule(fragment)
        val scope = Toothpick.openScopes(ApplicationScope::class, DiscussionCommentsScope::class, fragment)
        scope.closeOnDestroy(fragment).installModules(module).inject(fragment)
    }
}