package com.makentoshe.habrachan.application.android.screen.comments.di.module

import android.content.Context
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.comments.DiscussionCommentsFragment
import com.makentoshe.habrachan.application.android.screen.comments.di.CommentsScope
import com.makentoshe.habrachan.application.android.screen.comments.di.provider.DiscussionCommentsViewModelFactoryProvider
import com.makentoshe.habrachan.application.android.screen.comments.viewmodel.DiscussionCommentsViewModel
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind

class DiscussionCommentsModule(private val fragment: DiscussionCommentsFragment) : Module() {

    init {
        Toothpick.openScopes(ApplicationScope::class, CommentsScope::class).inject(this)
        bind<Context>().toInstance(fragment.requireActivity())

        bind<DiscussionCommentsViewModel.Factory>().toProvider(DiscussionCommentsViewModelFactoryProvider::class)
    }
}
