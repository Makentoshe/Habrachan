package com.makentoshe.habrachan.application.android.screen.articles.flow.di

import com.makentoshe.habrachan.application.android.common.di.FragmentInjector
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.articles.di.ArticlesModule
import com.makentoshe.habrachan.application.android.screen.articles.di.ArticlesScope
import com.makentoshe.habrachan.application.android.screen.articles.flow.ArticlesFlowFragment
import toothpick.Scope
import toothpick.Toothpick
import toothpick.smoothie.lifecycle.closeOnDestroy

class ArticlesFlowFragmentInjector: FragmentInjector<ArticlesFlowFragment>(
    fragmentClass = ArticlesFlowFragment::class,
) {

    // separated for all comment screens and for specified article screen
    override fun inject(injectorScope: FragmentInjectorScope<ArticlesFlowFragment>) {
        val scope = ArticlesFlowScope::class

        val toothpickScope = articlesScope(injectorScope).openSubScope(scope)
        val module = ArticlesFlowModule(injectorScope.fragment)
        captureModuleInstall(module, scope, injectorScope)
        toothpickScope.closeOnDestroy(injectorScope.fragment).installModules(module).inject(injectorScope.fragment)
    }

    private fun articlesScope(injectorScope: FragmentInjectorScope<ArticlesFlowFragment>): Scope {
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
