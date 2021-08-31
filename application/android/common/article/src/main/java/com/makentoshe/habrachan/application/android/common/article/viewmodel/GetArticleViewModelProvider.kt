package com.makentoshe.habrachan.application.android.common.article.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.application.android.common.di.provider.ViewModelFragmentProvider
import javax.inject.Inject

class GetArticleViewModelProvider @Inject constructor(
    private val factory: GetArticleViewModel.Factory,
) : ViewModelFragmentProvider<GetArticleViewModel> {
    override fun get(fragment: Fragment): GetArticleViewModel {
        return ViewModelProviders.of(fragment, factory)[GetArticleViewModel::class.java]
    }
}