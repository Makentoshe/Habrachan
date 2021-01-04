package com.makentoshe.habrachan.application.android.screen.comments.di

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.comments.ArticleCommentsFragment
import com.makentoshe.habrachan.application.android.screen.comments.RepliesCommentsFragment
import toothpick.Toothpick
import toothpick.smoothie.lifecycle.closeOnDestroy

class CommentsInjectingFragmentLifecycleCallback : FragmentManager.FragmentLifecycleCallbacks() {

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) = when (f) {
        is ArticleCommentsFragment -> injectArticleCommentsFragment(f)
        is RepliesCommentsFragment -> injectRepliesCommentsFragment(f)
        else -> Unit
    }

    private fun injectArticleCommentsFragment(fragment: ArticleCommentsFragment) {
        val module = ArticleCommentsModule(fragment)
        val scope = Toothpick.openScopes(ApplicationScope::class, ArticleCommentsScope::class)
        scope.closeOnDestroy(fragment).installModules(module).inject(fragment)
    }

    private fun injectRepliesCommentsFragment(fragment: RepliesCommentsFragment) {
        val module = RepliesCommentsModule(fragment)
        val scope = Toothpick.openScopes(ApplicationScope::class, RepliesCommentsScope::class, fragment)
        scope.closeOnDestroy(fragment).installModules(module).inject(fragment)
    }
}