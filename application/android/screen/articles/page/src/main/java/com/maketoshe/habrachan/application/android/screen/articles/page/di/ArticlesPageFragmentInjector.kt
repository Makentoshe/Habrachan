package com.maketoshe.habrachan.application.android.screen.articles.page.di

import com.makentoshe.habrachan.application.android.common.di.FragmentInjector
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.articles.di.ArticlesModule
import com.makentoshe.habrachan.application.android.screen.articles.di.ArticlesScope
import com.maketoshe.habrachan.application.android.screen.articles.page.ArticlesPageFragment
import toothpick.Scope
import toothpick.Toothpick
import toothpick.smoothie.lifecycle.closeOnDestroy

class ArticlesPageFragmentInjector : FragmentInjector<ArticlesPageFragment>(
    fragmentClass = ArticlesPageFragment::class,
) {

    // separated for all comment screens and for specified article screen
    override fun inject(injectorScope: FragmentInjectorScope<ArticlesPageFragment>) {
        val scope = ArticlesPageScope(injectorScope.fragment.arguments.index)

        val toothpickScope = articlesPageScope(injectorScope).openSubScope(scope)
        val module = SpecifiedArticlesPageModule(injectorScope.fragment)
        captureModuleInstall(module, scope, injectorScope)
        toothpickScope.closeOnDestroy(injectorScope.fragment).installModules(module).inject(injectorScope.fragment)
    }

    private fun articlesPageScope(injectorScope: FragmentInjectorScope<ArticlesPageFragment>): Scope {
        val scope = ArticlesPageScope::class
        if (Toothpick.isScopeOpen(scope)) {
            captureScopeOpened(scope, injectorScope)
            return Toothpick.openScopes(ApplicationScope::class, ArticlesScope::class, scope)
        }

        val toothpickScope = articlesScope(injectorScope).openSubScope(scope)
        val module = CommonArticlesPageModule()
        captureModuleInstall(module, scope, injectorScope)
        return toothpickScope.installModules(module)
    }

    private fun articlesScope(injectorScope: FragmentInjectorScope<ArticlesPageFragment>): Scope {
        val scope = ArticlesScope::class
        if (Toothpick.isScopeOpen(scope)) {
            captureScopeOpened(scope, injectorScope)
            return Toothpick.openScopes(ApplicationScope::class, scope)
        }

        val toothpickScope = Toothpick.openScopes(ApplicationScope::class, scope)
        val module = ArticlesModule()
        captureModuleInstall(module, scope, injectorScope)
        return toothpickScope.installModules(module)
    }
}
