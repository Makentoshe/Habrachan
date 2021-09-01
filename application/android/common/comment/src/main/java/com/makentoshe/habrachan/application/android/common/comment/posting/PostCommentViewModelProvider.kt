package com.makentoshe.habrachan.application.android.common.comment.posting

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.application.android.common.di.provider.ViewModelFragmentProvider
import javax.inject.Inject

class PostCommentViewModelProvider @Inject constructor(
    private val factory: PostCommentViewModel.Factory
): ViewModelFragmentProvider<PostCommentViewModel> {
    override fun get(fragment: Fragment): PostCommentViewModel {
        return ViewModelProviders.of(fragment, factory)[PostCommentViewModel::class.java]
    }
}