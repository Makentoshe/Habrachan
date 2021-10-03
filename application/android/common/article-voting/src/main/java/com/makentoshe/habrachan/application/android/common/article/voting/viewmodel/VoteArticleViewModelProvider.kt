package com.makentoshe.habrachan.application.android.common.article.voting.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.application.android.common.di.provider.ViewModelFragmentProvider
import javax.inject.Inject

class VoteArticleViewModelProvider @Inject constructor(
    private val factory: VoteArticleViewModel.Factory,
) : ViewModelFragmentProvider<VoteArticleViewModel> {
    override fun get(fragment: Fragment): VoteArticleViewModel {
        return ViewModelProviders.of(fragment, factory)[VoteArticleViewModel::class.java]
    }
}