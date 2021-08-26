package com.makentoshe.habrachan.application.android.screen.comments.di

import com.makentoshe.habrachan.application.android.common.di.FragmentInjector
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.comments.thread.ThreadCommentsFragment
import com.makentoshe.habrachan.application.android.screen.comments.thread.di.SpecifiedThreadCommentsModule
import com.makentoshe.habrachan.application.android.screen.comments.thread.di.ThreadCommentsModule
import com.makentoshe.habrachan.application.android.screen.comments.thread.di.ThreadCommentsScope
import toothpick.Scope
import toothpick.Toothpick
import toothpick.smoothie.lifecycle.closeOnDestroy

class ThreadCommentsFragmentInjector : FragmentInjector<ThreadCommentsFragment>(
    fragmentClass = ThreadCommentsFragment::class
) {
    override fun inject(injectorScope: FragmentInjectorScope<ThreadCommentsFragment>) {
        val scope = ThreadCommentsScope(injectorScope.fragment.arguments.commentId)

        val toothpickScope = discussionCommentsScope(injectorScope).openSubScope(scope)
        val module = SpecifiedThreadCommentsModule(injectorScope.fragment)
        captureModuleInstall(module, scope, injectorScope)
        toothpickScope.closeOnDestroy(injectorScope.fragment).installModules(module).inject(injectorScope.fragment)
    }

    private fun commentsScope(injectorScope: FragmentInjectorScope<ThreadCommentsFragment>): Scope {
        val scope = CommentsScope::class
        if (Toothpick.isScopeOpen(scope)) {
            captureScopeOpened(scope, injectorScope)
            return Toothpick.openScopes(ApplicationScope::class, scope)
        }

        val toothpickScope = Toothpick.openScopes(ApplicationScope::class, scope)
        val module = CommentsModule(injectorScope.fragment.arguments.articleId, "Title", injectorScope.fragment)
        captureModuleInstall(module, scope, injectorScope)
        return toothpickScope.installModules(module)
    }

    private fun discussionCommentsScope(injectorScope: FragmentInjectorScope<ThreadCommentsFragment>): Scope {
        val scope = ThreadCommentsScope::class
        if (Toothpick.isScopeOpen(scope)) {
            captureScopeOpened(scope, injectorScope)
            return Toothpick.openScopes(ApplicationScope::class, CommentsScope::class, scope)
        }

        val toothpickScope = commentsScope(injectorScope).openSubScope(scope)
        val module = ThreadCommentsModule(injectorScope.fragment)
        captureModuleInstall(module, scope, injectorScope)
        return toothpickScope.installModules(module)
    }
}