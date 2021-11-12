package com.makentoshe.habrachan.application.android.screen.articles.di.module

import com.makentoshe.habrachan.api.articles.ArticlesFilter
import com.makentoshe.habrachan.application.android.common.articles.viewmodel.GetArticlesSpec
import com.makentoshe.habrachan.application.android.common.articles.viewmodel.GetArticlesViewModel
import com.makentoshe.habrachan.application.android.database.user.UserSessionDatabase
import com.makentoshe.habrachan.application.android.database.user.record.toArticlesFilter
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.exception.ExceptionHandler
import com.makentoshe.habrachan.application.android.screen.articles.ArticlesPageFragment
import com.makentoshe.habrachan.application.android.screen.articles.di.ArticlesScope
import com.makentoshe.habrachan.application.android.screen.articles.di.provider.GetArticlesViewModelProvider
import com.makentoshe.habrachan.application.android.screen.articles.di.scope.ArticlesPageScope
import com.makentoshe.habrachan.application.android.screen.articles.model.ArticlesPageAdapter
import com.makentoshe.habrachan.application.android.screen.articles.model.ArticlesPageFooterAdapter
import com.makentoshe.habrachan.functional.Option2
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class SpecifiedArticlesPageModule(fragment: ArticlesPageFragment) : Module() {

    private val userDatabase by inject<UserSessionDatabase>()

    init {
        Toothpick.openScopes(ApplicationScope::class, ArticlesScope::class, ArticlesPageScope::class).inject(this)
        bind<ArticlesPageFragment>().toInstance(fragment)

        bind<Option2<GetArticlesSpec>>().toInstance(Option2.Value(GetArticlesSpec(articleFilters(fragment.arguments))))
        bind<ExceptionHandler>().toInstance(ExceptionHandler(fragment.requireContext()))

        bind<GetArticlesViewModel>().toProvider(GetArticlesViewModelProvider::class).singleton()
        bind<ArticlesPageAdapter>().toClass<ArticlesPageAdapter>().singleton()
        bind<ArticlesPageFooterAdapter>().toClass<ArticlesPageFooterAdapter>().singleton()
    }

    private fun articleFilters(arguments: ArticlesPageFragment.Arguments): Set<ArticlesFilter> {
        val articlesUserSearch = userDatabase.articlesUserSearchDao().getByIndex(arguments.index)!!
        return userDatabase.articlesUserSearchArticlesFilterCrossRef().getByTitle(articlesUserSearch.title)
            .mapNotNull { crossRef ->
                userDatabase.articlesFilterDao().getByKeyValue(crossRef.keyValuePair)
            }.mapNotNull { filterRecord ->
                filterRecord.toArticlesFilter().getOrNull()
            }.toSet()
    }
}