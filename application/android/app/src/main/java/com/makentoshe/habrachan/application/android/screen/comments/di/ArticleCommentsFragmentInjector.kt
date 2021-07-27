package com.makentoshe.habrachan.application.android.screen.comments.di

import com.makentoshe.habrachan.application.android.common.di.FragmentInjector
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.comments.ArticleCommentsFragment
import toothpick.Toothpick
import toothpick.smoothie.lifecycle.closeOnDestroy

class ArticleCommentsFragmentInjector: FragmentInjector<ArticleCommentsFragment>(
    fragmentClass = ArticleCommentsFragment::class
) {
    override fun inject(injectorScope: FragmentInjectorScope<ArticleCommentsFragment>) {
        val scope = CommentsScope.Article(injectorScope.fragment.arguments.articleId)
        val module = ArticleCommentsModule(injectorScope.fragment) // TODO separate module for all Articles and for concrete article
        val toothpickScope = Toothpick.openScopes(ApplicationScope::class, CommentsScope::class, scope)
        toothpickScope.closeOnDestroy(injectorScope.fragment).installModules(module).inject(injectorScope.fragment)
    }
}

sealed class CommentsScope {
    data class Article(val articleId: Int): CommentsScope()
    data class Discussion(val commentId: Int): CommentsScope()
}