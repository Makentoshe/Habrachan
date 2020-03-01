package com.makentoshe.habrachan.di.post

import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.model.post.AdvancedWebViewController
import com.makentoshe.habrachan.view.post.ArticleFragment
import com.makentoshe.habrachan.viewmodel.post.ArticleFragmentViewModel
import com.makentoshe.habrachan.viewmodel.post.VoteArticleViewModel
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind

class ArticleFragmentModule(fragment: ArticleFragment) : Module() {

    init {
        Toothpick.openScope(ApplicationScope::class.java).inject(this)

        bind<ArticleFragmentViewModel>().toProviderInstance(ArticleFragmentViewModelProvider(fragment))
        bind<VoteArticleViewModel>().toProviderInstance(VoteArticleViewModelProvider(fragment))
        bind<AdvancedWebViewController>().toInstance(
            AdvancedWebViewController(fragment)
        )
    }
}