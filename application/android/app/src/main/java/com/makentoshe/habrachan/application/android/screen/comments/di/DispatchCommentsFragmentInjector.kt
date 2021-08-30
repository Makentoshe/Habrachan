package com.makentoshe.habrachan.application.android.screen.comments.di

import com.makentoshe.habrachan.application.android.common.di.FragmentInjector
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.comments.dispatch.DispatchCommentsFragment
import com.makentoshe.habrachan.application.android.screen.comments.dispatch.di.DispatchCommentsScope
import com.makentoshe.habrachan.application.android.screen.comments.dispatch.di.module.DispatchCommentsModule
import com.makentoshe.habrachan.application.android.screen.comments.dispatch.di.module.SpecifiedDispatchCommentsModule
import toothpick.Scope
import toothpick.Toothpick
import toothpick.smoothie.lifecycle.closeOnDestroy

class DispatchCommentsFragmentInjector : FragmentInjector<DispatchCommentsFragment>(
    fragmentClass = DispatchCommentsFragment::class
) {

    override fun inject(injectorScope: FragmentInjectorScope<DispatchCommentsFragment>) {
        val scope = DispatchCommentsScope(injectorScope.fragment.arguments.articleId, injectorScope.fragment.arguments.commentId)

        val toothpickScope = dispatchCommentsScope(injectorScope).openSubScope(scope)
        val module = SpecifiedDispatchCommentsModule(injectorScope.fragment)
        captureModuleInstall(module, scope, injectorScope)
        toothpickScope.closeOnDestroy(injectorScope.fragment).installModules(module).inject(injectorScope.fragment)
    }

    private fun dispatchCommentsScope(injectorScope: FragmentInjectorScope<DispatchCommentsFragment>): Scope {
        val scope = DispatchCommentsScope::class
        if (Toothpick.isScopeOpen(scope)) {
            captureScopeOpened(scope, injectorScope)
            return Toothpick.openScopes(ApplicationScope::class, CommentsScope::class, scope)
        }

        val toothpickScope = commentsScope(injectorScope).openSubScope(scope)
        val module = DispatchCommentsModule()
        captureModuleInstall(module, scope, injectorScope)
        return toothpickScope.installModules(module)
    }

    private fun commentsScope(injectorScope: FragmentInjectorScope<DispatchCommentsFragment>): Scope {
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