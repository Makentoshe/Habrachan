package com.makentoshe.habrachan.application.android.screen.article.di

import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.common.di.FragmentInjector
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.article.ArticleFragment
import toothpick.Scope
import toothpick.Toothpick
import toothpick.smoothie.lifecycle.closeOnDestroy

class ArticleFragmentInjector : FragmentInjector<ArticleFragment>(fragmentClass = ArticleFragment::class) {

    companion object : Analytics(LogAnalytic())

    override fun inject(injectorScope: FragmentInjectorScope<ArticleFragment>) {
        val scope = ArticleScope(injectorScope.fragment.arguments.articleId)
        val toothpickScope = commonArticleScope(injectorScope).openSubScope(scope)
        val module = SpecifiedArticleModule(injectorScope)
        toothpickScope.installModules(module).closeOnDestroy(injectorScope.fragment).inject(injectorScope.fragment)

        capture(analyticEvent {
            "Inject ${module::class.simpleName} in scope $scope to ${injectorScope.fragment}"
        })
    }

    private fun commonArticleScope(injectorScope: FragmentInjectorScope<ArticleFragment>): Scope {
        if (Toothpick.isScopeOpen(ArticleScope::class)) {
            return Toothpick.openScopes(ApplicationScope::class, ArticleScope::class)
        }

        val module = CommonArticleModule(injectorScope)
        val scope = Toothpick.openScopes(ApplicationScope::class, ArticleScope::class).installModules(module)

        capture(analyticEvent {
            "Inject ${module::class.simpleName} in scope ${ArticleScope::class.simpleName} to ${injectorScope.fragment}"
        })

        return scope
    }
}
