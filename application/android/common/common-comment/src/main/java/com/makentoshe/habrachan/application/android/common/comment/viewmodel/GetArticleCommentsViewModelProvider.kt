package com.makentoshe.habrachan.application.android.common.comment.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.application.android.common.comment.di.provider.ViewModelKeyProvider

class GetArticleCommentsViewModelProvider(
    private val fragment: Fragment,
    private val factory: GetArticleCommentsViewModel.Factory,
) : ViewModelKeyProvider<GetArticleCommentsViewModel> {

    override fun get(key: String): GetArticleCommentsViewModel {
        return ViewModelProviders.of(fragment, factory).get(key, GetArticleCommentsViewModel::class.java)
    }
}