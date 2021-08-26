package com.makentoshe.habrachan.application.android.screen.comments.di

import com.makentoshe.habrachan.application.android.common.di.FragmentInjector
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.comments.discussion.DiscussionCommentsFragment
import com.makentoshe.habrachan.application.android.screen.comments.discussion.di.DiscussionCommentsModule
import com.makentoshe.habrachan.application.android.screen.comments.discussion.di.DiscussionCommentsScope2
import com.makentoshe.habrachan.application.android.screen.comments.discussion.di.SpecifiedDiscussionCommentsModule
import toothpick.Scope
import toothpick.Toothpick
import toothpick.smoothie.lifecycle.closeOnDestroy

class DiscussionCommentsFragmentInjector : FragmentInjector<DiscussionCommentsFragment>(
    fragmentClass = DiscussionCommentsFragment::class
) {
    override fun inject(injectorScope: FragmentInjectorScope<DiscussionCommentsFragment>) {
        val scope = DiscussionCommentsScope2(injectorScope.fragment.arguments.commentId)

        val toothpickScope = discussionCommentsScope(injectorScope).openSubScope(scope)
        val module = SpecifiedDiscussionCommentsModule(injectorScope.fragment)
        captureModuleInstall(module, scope, injectorScope)
        toothpickScope.closeOnDestroy(injectorScope.fragment).installModules(module).inject(injectorScope.fragment)
    }

    private fun commentsScope(injectorScope: FragmentInjectorScope<DiscussionCommentsFragment>): Scope {
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

    private fun discussionCommentsScope(injectorScope: FragmentInjectorScope<DiscussionCommentsFragment>): Scope {
        val scope = DiscussionCommentsScope2::class
        if (Toothpick.isScopeOpen(scope)) {
            captureScopeOpened(scope, injectorScope)
            return Toothpick.openScopes(ApplicationScope::class, CommentsScope::class, scope)
        }

        val toothpickScope = commentsScope(injectorScope).openSubScope(scope)
        val module = DiscussionCommentsModule(injectorScope.fragment)
        captureModuleInstall(module, scope, injectorScope)
        return toothpickScope.installModules(module)
    }
}