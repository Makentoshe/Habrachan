package com.makentoshe.habrachan.application.android.common.comment.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.application.android.common.comment.di.provider.ViewModelFragmentProvider

class GetArticleCommentsViewModelProvider(
    private val factory: GetArticleCommentsViewModel.Factory,
) : ViewModelFragmentProvider<GetArticleCommentsViewModel> {

    override fun get(fragment: Fragment): GetArticleCommentsViewModel {
        return ViewModelProviders.of(fragment, factory).get(GetArticleCommentsViewModel::class.java)
    }
}