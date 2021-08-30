package com.makentoshe.habrachan.application.android.screen.comments.di

import com.makentoshe.habrachan.application.android.common.di.FragmentInjector
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.comments.articles.ArticleCommentsFragment
import com.makentoshe.habrachan.application.android.screen.comments.articles.di.ArticleCommentsScope2
import com.makentoshe.habrachan.application.android.screen.comments.articles.di.module.ArticleCommentsModule
import com.makentoshe.habrachan.application.android.screen.comments.articles.di.module.SpecifiedArticleCommentsModule
import toothpick.Scope
import toothpick.Toothpick
import toothpick.smoothie.lifecycle.closeOnDestroy

class ArticleCommentsFragmentInjector : FragmentInjector<ArticleCommentsFragment>(
    fragmentClass = ArticleCommentsFragment::class
) {

    // separated for all comment screens and for specified article screen
    override fun inject(injectorScope: FragmentInjectorScope<ArticleCommentsFragment>) {
        val scope = ArticleCommentsScope2(injectorScope.fragment.arguments.articleId)

        val toothpickScope = articleCommentsScope(injectorScope).openSubScope(scope)
        val module = SpecifiedArticleCommentsModule(injectorScope.fragment)
        captureModuleInstall(module, scope, injectorScope)
        toothpickScope.closeOnDestroy(injectorScope.fragment).installModules(module).inject(injectorScope.fragment)
    }

    private fun articleCommentsScope(injectorScope: FragmentInjectorScope<ArticleCommentsFragment>): Scope {
        val scope = ArticleCommentsScope2::class
        if (Toothpick.isScopeOpen(scope)) {
            captureScopeOpened(scope, injectorScope)
            return Toothpick.openScopes(ApplicationScope::class, CommentsScope::class, scope)
        }

        val toothpickScope = commentsScope(injectorScope).openSubScope(scope)
        val module = ArticleCommentsModule()
        captureModuleInstall(module, scope, injectorScope)
        return toothpickScope.installModules(module)
    }

    private fun commentsScope(injectorScope: FragmentInjectorScope<ArticleCommentsFragment>): Scope {
        val scope = CommentsScope::class
        if (Toothpick.isScopeOpen(scope)) {
            captureScopeOpened(scope, injectorScope)
            return Toothpick.openScopes(ApplicationScope::class, scope)
        }

        val toothpickScope = Toothpick.openScopes(ApplicationScope::class, scope)
        val module = CommentsModule(injectorScope.fragment.arguments.articleId, injectorScope.fragment)
        captureModuleInstall(module, scope, injectorScope)
        return toothpickScope.installModules(module)
    }
}
