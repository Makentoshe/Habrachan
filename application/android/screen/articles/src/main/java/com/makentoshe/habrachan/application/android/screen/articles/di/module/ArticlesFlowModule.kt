package com.makentoshe.habrachan.application.android.screen.articles.di.module

import android.content.res.Resources
import com.makentoshe.habrachan.application.android.database.user.UserSessionDatabase
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.articles.ArticlesFlowFragment
import com.makentoshe.habrachan.application.android.screen.articles.di.ArticlesScope
import com.makentoshe.habrachan.application.android.screen.articles.di.provider.ArticlesFlowAdapterProvider
import com.makentoshe.habrachan.application.android.screen.articles.model.TabLayoutMediatorController
import com.makentoshe.habrachan.application.android.screen.articles.model.toArticlesUserSearch
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

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