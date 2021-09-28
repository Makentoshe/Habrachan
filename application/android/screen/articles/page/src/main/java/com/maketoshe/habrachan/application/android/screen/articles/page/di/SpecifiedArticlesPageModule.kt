package com.maketoshe.habrachan.application.android.screen.articles.page.di

import com.makentoshe.habrachan.application.android.common.articles.viewmodel.GetArticlesSpec
import com.makentoshe.habrachan.application.android.common.articles.viewmodel.GetArticlesViewModel
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.exception.ExceptionHandler
import com.makentoshe.habrachan.application.android.screen.articles.di.ArticlesScope
import com.makentoshe.habrachan.functional.Option
import com.maketoshe.habrachan.application.android.screen.articles.page.ArticlesPageFragment
import com.maketoshe.habrachan.application.android.screen.articles.page.di.provider.GetArticlesViewModelProvider
import com.maketoshe.habrachan.application.android.screen.articles.page.model.ArticlesFooterAdapter
import com.maketoshe.habrachan.application.android.screen.articles.page.model.ArticlesPageAdapter
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind

class SpecifiedArticlesPageModule(fragment: ArticlesPageFragment) : Module() {
    init {
        Toothpick.openScopes(ApplicationScope::class, ArticlesScope::class, ArticlesPageScope::class).inject(this)
        bind<ArticlesPageFragment>().toInstance(fragment)
        val getArticlesSpec = GetArticlesSpec(page = 1, specType = fragment.arguments.spec)
        bind<Option<GetArticlesSpec>>().toInstance(Option.Value(getArticlesSpec))

        bind<ExceptionHandler>().toInstance(ExceptionHandler(fragment.requireContext()))

        bind<GetArticlesViewModel>().toProvider(GetArticlesViewModelProvider::class).singleton()
        bind<ArticlesPageAdapter>().toClass<ArticlesPageAdapter>().singleton()
        bind<ArticlesFooterAdapter>().toClass<ArticlesFooterAdapter>().singleton()
    }
}