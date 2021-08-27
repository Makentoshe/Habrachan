package com.makentoshe.habrachan.application.android.screen.comments.dispatch.di.module

import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.comments.di.CommentsScope
import com.makentoshe.habrachan.application.android.screen.comments.dispatch.DispatchCommentsFragment
import toothpick.Toothpick
import toothpick.config.Module

class DispatchCommentsModule(fragment: DispatchCommentsFragment) : Module() {

    init {
        Toothpick.openScopes(ApplicationScope::class, CommentsScope::class).inject(this)
    }
}