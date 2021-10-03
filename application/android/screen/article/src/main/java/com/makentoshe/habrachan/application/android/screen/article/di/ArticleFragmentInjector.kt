package com.makentoshe.habrachan.application.android.screen.article.di

import com.makentoshe.habrachan.application.android.common.di.FragmentInjector
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.article.ArticleFragment
import toothpick.Scope
import toothpick.Toothpick
import toothpick.smoothie.lifecycle.closeOnDestroy

class ArticleFragmentInjector: FragmentInjector<ArticleFragment>(
    fragmentClass = ArticleFragment::class,
) {

    // separated for all comment screens and for specified article screen
    override fun inject(injectorScope: FragmentInjectorScope<ArticleFragment>) {
        val scope = ArticleScope(injectorScope.fragment.arguments.articleId)

        val toothpickScope = articleScope(injectorScope).openSubScope(scope)
        val module = SpecifiedArticleModule(injectorScope.fragment)
        captureModuleInstall(module, scope, injectorScope)
        toothpickScope.closeOnDestroy(injectorScope.fragment).installModules(module).inject(injectorScope.fragment)
    }

    private fun articleScope(injectorScope: FragmentInjectorScope<ArticleFragment>): Scope {
        val scope = ArticleScope::class
        if (Toothpick.isScopeOpen(scope)) {
            captureScopeOpened(scope, injectorScope)
            return Toothpick.openScopes(ApplicationScope::class, scope)
        }

        val toothpickScope = Toothpick.openScopes(ApplicationScope::class, scope)
        val module = CommonArticleModule(injectorScope.fragment)
        captureModuleInstall(module, scope, injectorScope)
        return toothpickScope.installModules(module)
    }
}
