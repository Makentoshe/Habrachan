package com.makentoshe.habrachan.application.android.screen.comments.di

import com.makentoshe.habrachan.application.android.common.di.FragmentInjector
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.comments.ArticleCommentsFragment
import com.makentoshe.habrachan.application.android.screen.comments.di.module.GetArticleCommentsNetworkModule
import com.makentoshe.habrachan.application.android.screen.comments.di.module.GetArticleNetworkModule
import com.makentoshe.habrachan.application.android.screen.comments.di.module.GetAvatarArenaModule
import toothpick.Scope
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.smoothie.lifecycle.closeOnDestroy

class ArticleCommentsFragmentInjector : FragmentInjector<ArticleCommentsFragment>(
    fragmentClass = ArticleCommentsFragment::class
) {

    override fun inject(injectorScope: FragmentInjectorScope<ArticleCommentsFragment>) {
        val scope = CommentsScope(injectorScope.fragment.arguments.articleId)

        val toothpickScope = commonCommentsScope(injectorScope).openSubScope(scope)
        val screenModule = CommentsScreenModule(injectorScope)
        captureModuleInstall(screenModule, scope, injectorScope)
        toothpickScope.installModules(screenModule).closeOnDestroy(injectorScope.fragment).inject(injectorScope.fragment)
    }

    private fun commonCommentsScope(injectorScope: FragmentInjectorScope<ArticleCommentsFragment>) : Scope {
        val scope = CommentsScope::class
        if (Toothpick.isScopeOpen(scope)) {
            captureScopeOpened(scope, injectorScope)
            return Toothpick.openScopes(ApplicationScope::class, scope)
        }

        return Toothpick.openScopes(ApplicationScope::class, scope).apply {
            installModule(GetAvatarArenaModule(), injectorScope)
            installModule(GetArticleNetworkModule(), injectorScope)
            installModule(GetArticleCommentsNetworkModule(), injectorScope)
        }
    }

    private fun Scope.installModule(module: Module, injectorScope: FragmentInjectorScope<*>) {
        installModules(module); captureModuleInstall(module, this, injectorScope)
    }
}
