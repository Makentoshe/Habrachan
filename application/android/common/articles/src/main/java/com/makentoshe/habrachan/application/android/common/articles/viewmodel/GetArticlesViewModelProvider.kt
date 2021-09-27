package com.makentoshe.habrachan.application.android.common.articles.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.application.android.common.di.provider.ViewModelFragmentProvider
import javax.inject.Inject

class GetArticlesViewModelProvider @Inject constructor(
    private val factory: GetArticlesViewModel.Factory,
) : ViewModelFragmentProvider<GetArticlesViewModel> {
    override fun get(fragment: Fragment): GetArticlesViewModel {
        return ViewModelProviders.of(fragment, factory)[GetArticlesViewModel::class.java]
    }
}