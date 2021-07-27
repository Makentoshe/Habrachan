package com.makentoshe.habrachan.application.android.screen.comments.di

import com.makentoshe.habrachan.application.android.common.di.FragmentInjector
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.comments.CommentDetailsDialogFragment
import toothpick.Toothpick
import toothpick.smoothie.lifecycle.closeOnDestroy

class CommentDetailsFragmentModule: FragmentInjector<CommentDetailsDialogFragment>(
    fragmentClass = CommentDetailsDialogFragment::class,
    action = {
        val module = CommentDetailsModule(fragment)
        val scope = Toothpick.openScopes(ApplicationScope::class, CommentDetailsScope::class)
        scope.closeOnDestroy(fragment).installModules(module).inject(fragment)
    }
)