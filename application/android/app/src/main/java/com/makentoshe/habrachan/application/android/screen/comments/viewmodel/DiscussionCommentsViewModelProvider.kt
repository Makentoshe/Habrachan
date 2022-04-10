package com.makentoshe.habrachan.application.android.screen.comments.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.application.android.common.comment.di.provider.ViewModelFragmentProvider
import javax.inject.Inject

internal class DiscussionCommentsViewModelProvider(
    private val factory: DiscussionCommentsViewModel.Factory
) : ViewModelFragmentProvider<DiscussionCommentsViewModel> {

    @Inject

    override fun get(fragment: Fragment): DiscussionCommentsViewModel {
        return ViewModelProviders.of(fragment, factory)[DiscussionCommentsViewModel::class.java]
    }
}