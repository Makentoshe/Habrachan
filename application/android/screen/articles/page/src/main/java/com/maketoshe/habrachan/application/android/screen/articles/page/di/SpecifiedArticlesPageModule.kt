package com.maketoshe.habrachan.application.android.screen.articles.page.di

import com.makentoshe.habrachan.application.android.common.articles.viewmodel.GetArticlesSpec
import com.makentoshe.habrachan.application.android.common.articles.viewmodel.GetArticlesViewModel
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.articles.di.ArticlesScope
import com.makentoshe.habrachan.functional.Option
import com.maketoshe.habrachan.application.android.screen.articles.page.ArticlesPageFragment
import com.maketoshe.habrachan.application.android.screen.articles.page.di.provider.GetArticlesViewModelProvider
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind

class SpecifiedArticlesPageModule(fragment: ArticlesPageFragment): Module() {
    init {
        Toothpick.openScopes(ApplicationScope::class, ArticlesScope::class, ArticlesPageScope::class).inject(this)
        bind<ArticlesPageFragment>().toInstance(fragment)
        bind<Option<GetArticlesSpec>>().toInstance(Option.None)

        bind<GetArticlesViewModel>().toProvider(GetArticlesViewModelProvider::class).singleton()
    }
}