package com.makentoshe.habrachan.application.android.screen.article.di

import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.common.di.FragmentInjector
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.article.ArticleFragment
import com.makentoshe.habrachan.application.android.screen.article.di.module.*
import toothpick.Scope
import toothpick.Toothpick
import toothpick.smoothie.lifecycle.closeOnDestroy

class ArticleFragmentInjector : FragmentInjector<ArticleFragment>(fragmentClass = ArticleFragment::class) {

    companion object : Analytics(LogAnalytic())

    override fun inject(injectorScope: FragmentInjectorScope<ArticleFragment>) {
        val scope = ArticleScope(injectorScope.fragment.arguments.articleId)

        val toothpickScope = commonArticleScope(injectorScope).openSubScope(scope)
        val screenModule = ArticleScreenModule(injectorScope)
        captureModuleInstall(screenModule, scope, injectorScope)
        val avatarViewModelModule = GetAvatarViewModelModule(injectorScope.fragment)
        captureModuleInstall(avatarViewModelModule, scope, injectorScope)
        toothpickScope.installModules(screenModule, avatarViewModelModule).closeOnDestroy(injectorScope.fragment).inject(injectorScope.fragment)
    }

    private fun commonArticleScope(injectorScope: FragmentInjectorScope<ArticleFragment>): Scope {
        val scope = ArticleScope::class
        if (Toothpick.isScopeOpen(scope)) {
            captureScopeOpened(scope, injectorScope)
            return Toothpick.openScopes(ApplicationScope::class, scope)
        }

        val toothpickScope = Toothpick.openScopes(ApplicationScope::class, scope)
        val getArticleNetworkModule = GetArticleNetworkModule()
        captureModuleInstall(getArticleNetworkModule, scope, injectorScope)
        val voteArticleNetworkModule = VoteArticleNetworkModule()
        captureModuleInstall(voteArticleNetworkModule, scope, injectorScope)
        val getAvatarNetworkModule = GetAvatarNetworkModule()
        captureModuleInstall(getAvatarNetworkModule, scope, injectorScope)
        return toothpickScope.installModules(getArticleNetworkModule, voteArticleNetworkModule, getAvatarNetworkModule)
    }
}
