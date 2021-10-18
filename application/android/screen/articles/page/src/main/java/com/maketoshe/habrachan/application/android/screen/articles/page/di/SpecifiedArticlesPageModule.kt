package com.maketoshe.habrachan.application.android.screen.articles.page.di

import com.makentoshe.habrachan.api.articles.ArticlesFilter
import com.makentoshe.habrachan.application.android.common.articles.viewmodel.GetArticlesSpec
import com.makentoshe.habrachan.application.android.common.articles.viewmodel.GetArticlesViewModel
import com.makentoshe.habrachan.application.android.database.UserSessionDatabase
import com.makentoshe.habrachan.application.android.database.record.userdatabase.toArticlesFilter
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
import toothpick.ktp.delegate.inject

class SpecifiedArticlesPageModule(fragment: ArticlesPageFragment) : Module() {

    private val userDatabase by inject<UserSessionDatabase>()

    init {
        Toothpick.openScopes(ApplicationScope::class, ArticlesScope::class, ArticlesPageScope::class).inject(this)
        bind<ArticlesPageFragment>().toInstance(fragment)

        bind<Option<GetArticlesSpec>>().toInstance(Option.Value(GetArticlesSpec(articleFilters(fragment.arguments))))
        bind<ExceptionHandler>().toInstance(ExceptionHandler(fragment.requireContext()))

        bind<GetArticlesViewModel>().toProvider(GetArticlesViewModelProvider::class).singleton()
        bind<ArticlesPageAdapter>().toClass<ArticlesPageAdapter>().singleton()
        bind<ArticlesFooterAdapter>().toClass<ArticlesFooterAdapter>().singleton()
    }

    private fun articleFilters(arguments: ArticlesPageFragment.Arguments): Set<ArticlesFilter> {
        val articlesUserSearch = userDatabase.articlesUserSearchDao().getByIndex(arguments.index)!!
        return userDatabase.articlesUserSearchArticlesFilterCrossRef().getByTitle(articlesUserSearch.title).mapNotNull { crossRef ->
            userDatabase.articlesFilterDao().getByKeyValue(crossRef.keyValuePair)
        }.mapNotNull { filterRecord ->
            filterRecord.toArticlesFilter().getOrNull()
        }.toSet()
    }
}