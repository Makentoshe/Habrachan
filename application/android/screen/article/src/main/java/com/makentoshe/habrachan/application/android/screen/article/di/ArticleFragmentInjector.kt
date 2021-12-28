package com.makentoshe.habrachan.application.android.screen.article.di

import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.common.di.FragmentInjector
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.article.ArticleFragment
import com.makentoshe.habrachan.application.android.screen.article.di.module.ArticleScreenModule
import com.makentoshe.habrachan.application.android.screen.article.di.module.GetArticleNetworkModule
import com.makentoshe.habrachan.application.android.screen.article.di.module.GetAvatarNetworkModule
import com.makentoshe.habrachan.application.android.screen.article.di.module.VoteArticleNetworkModule
import toothpick.Scope
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.smoothie.lifecycle.closeOnDestroy

class ArticleFragmentInjector : FragmentInjector<ArticleFragment>(fragmentClass = ArticleFragment::class) {

    companion object : Analytics(LogAnalytic())

    override fun inject(injectorScope: FragmentInjectorScope<ArticleFragment>) {
        val scope = ArticleScope(injectorScope.fragment.arguments.articleId)

        val toothpickScope = commonArticleScope(injectorScope).openSubScope(scope)
        val screenModule = ArticleScreenModule(injectorScope)
        captureModuleInstall(screenModule, scope, injectorScope)
        toothpickScope.installModules(screenModule).closeOnDestroy(injectorScope.fragment).inject(injectorScope.fragment)
    }

    private fun commonArticleScope(injectorScope: FragmentInjectorScope<ArticleFragment>): Scope {
        val scope = ArticleScope::class
        if (Toothpick.isScopeOpen(scope)) {
            captureScopeOpened(scope, injectorScope)
            return Toothpick.openScopes(ApplicationScope::class, scope)
        }

        return Toothpick.openScopes(ApplicationScope::class, scope).apply {
            installModule(GetArticleNetworkModule(), injectorScope)
            installModule(VoteArticleNetworkModule(), injectorScope)
            installModule(GetAvatarNetworkModule(), injectorScope)
        }
    }

    private fun Scope.installModule(module: Module, injectorScope: FragmentInjectorScope<*>) {
        installModules(module); captureModuleInstall(module, this, injectorScope)
    }
}
