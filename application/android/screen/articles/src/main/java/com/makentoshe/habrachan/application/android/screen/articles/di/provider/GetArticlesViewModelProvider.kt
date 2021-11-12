package com.makentoshe.habrachan.application.android.screen.articles.di.provider

import com.makentoshe.habrachan.application.android.common.articles.viewmodel.GetArticlesViewModel
import com.makentoshe.habrachan.application.android.common.articles.viewmodel.GetArticlesViewModelProvider
import com.makentoshe.habrachan.application.android.screen.articles.ArticlesPageFragment
import javax.inject.Inject
import javax.inject.Provider

class GetArticlesViewModelProvider @Inject constructor(
    private val fragment: ArticlesPageFragment,
    private val viewModelProvider: GetArticlesViewModelProvider,
) : Provider<GetArticlesViewModel> {
    override fun get() = viewModelProvider.get(fragment)
}