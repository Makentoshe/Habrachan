package com.makentoshe.habrachan.application.android.screen.articles.flow.di

import android.content.res.Resources
import com.makentoshe.habrachan.application.android.database.UserSessionDatabase
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.articles.di.ArticlesScope
import com.makentoshe.habrachan.application.android.screen.articles.flow.ArticlesFlowFragment
import com.makentoshe.habrachan.application.android.screen.articles.flow.di.provider.ArticlesFlowAdapterProvider
import com.makentoshe.habrachan.application.android.screen.articles.flow.model.TabLayoutMediatorController
import com.makentoshe.habrachan.application.android.screen.articles.flow.model.toArticlesUserSearch
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject
import javax.inject.Qualifier

@Qualifier
annotation class ArticlesFlowScope

class ArticlesFlowModule(fragment: ArticlesFlowFragment) : Module() {

    private val userDatabase by inject<UserSessionDatabase>()

    init {
        Toothpick.openScopes(ApplicationScope::class, ArticlesScope::class).inject(this)
        bind<Resources>().toInstance(fragment.resources)
        bind<ArticlesFlowFragment>().toInstance(fragment)

        val searches = userDatabase.articlesUserSearchDao().getAll().map { it.toArticlesUserSearch() }
        bind<TabLayoutMediatorController>().toInstance(TabLayoutMediatorController(searches))

        bind<ArticlesFlowAdapterProvider>().toInstance(ArticlesFlowAdapterProvider(fragment))
    }
}