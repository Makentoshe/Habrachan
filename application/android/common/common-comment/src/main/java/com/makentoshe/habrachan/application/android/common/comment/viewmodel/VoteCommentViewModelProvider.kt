package com.makentoshe.habrachan.application.android.common.comment.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.application.android.common.di.provider.ViewModelKeyProvider
import javax.inject.Inject

class VoteCommentViewModelProvider @Inject constructor(
    private val fragment: Fragment,
    private val factory: VoteCommentViewModel.Factory,
) : ViewModelKeyProvider<VoteCommentViewModel> {

    override fun get(key: String): VoteCommentViewModel {
        return ViewModelProviders.of(fragment, factory).get(key, VoteCommentViewModel::class.java)
    }
}