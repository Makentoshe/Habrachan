package com.makentoshe.habrachan.application.android.common.comment.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders

class VoteCommentViewModelProvider(private val fragment: Fragment, private val factory: VoteCommentViewModel.Factory) {
    fun get(commentId: Int): VoteCommentViewModel {
        return ViewModelProviders.of(fragment, factory).get(commentId.toString(), VoteCommentViewModel::class.java)
    }
}