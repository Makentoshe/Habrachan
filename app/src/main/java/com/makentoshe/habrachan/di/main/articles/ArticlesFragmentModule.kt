package com.makentoshe.habrachan.di.main.articles

import com.makentoshe.habrachan.di.common.ApplicationScope
import com.makentoshe.habrachan.view.main.articles.ArticlesFragment
import com.makentoshe.habrachan.viewmodel.main.articles.ArticlesViewModel
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind

class ArticlesFragmentModule(fragment: ArticlesFragment) : Module() {

    init {
        Toothpick.openScope(ApplicationScope::class.java).inject(this)

        bind<ArticlesViewModel>().toProviderInstance(ArticlesViewModelProvider(fragment))
    }
}
