package com.makentoshe.habrachan.application.android.screen.comments.di

import com.makentoshe.habrachan.application.android.common.di.FragmentInjector
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.comments.DiscussionCommentsFragment
import toothpick.Toothpick
import toothpick.smoothie.lifecycle.closeOnDestroy

class DiscussionCommentsFragmentInjector: FragmentInjector<DiscussionCommentsFragment>(
    fragmentClass = DiscussionCommentsFragment::class
) {
    override fun inject(injectorScope: FragmentInjectorScope<DiscussionCommentsFragment>) {
        val scope = CommentsScope.Discussion(injectorScope.fragment.arguments.commentId)
        val module = DiscussionCommentsModule(injectorScope.fragment)
        val toothpickScope = Toothpick.openScopes(ApplicationScope::class, CommentsScope::class, scope)
        toothpickScope.closeOnDestroy(injectorScope.fragment).installModules(module).inject(injectorScope.fragment)
    }
}