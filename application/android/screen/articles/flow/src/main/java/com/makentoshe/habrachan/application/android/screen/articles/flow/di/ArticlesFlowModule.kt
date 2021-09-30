package com.makentoshe.habrachan.application.android.screen.articles.flow.di

import android.content.res.Resources
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.articles.di.ArticlesScope
import com.makentoshe.habrachan.application.android.screen.articles.flow.ArticlesFlowFragment
import com.makentoshe.habrachan.application.android.screen.articles.flow.di.provider.AvailableSpecTypesProvider
import com.makentoshe.habrachan.application.android.screen.articles.flow.model.ArticlesFlowAdapter
import com.makentoshe.habrachan.application.android.screen.articles.flow.model.AvailableSpecTypes
import com.makentoshe.habrachan.application.android.screen.articles.flow.model.TabLayoutMediatorController
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import javax.inject.Qualifier

@Qualifier
annotation class ArticlesFlowScope

class ArticlesFlowModule(fragment: ArticlesFlowFragment) : Module() {

    init {
        Toothpick.openScopes(ApplicationScope::class, ArticlesScope::class).inject(this)
        bind<Resources>().toInstance(fragment.resources)
        bind<ArticlesFlowFragment>().toInstance(fragment)

        bind<AvailableSpecTypes>().toProvider(AvailableSpecTypesProvider::class).singleton()
        bind<TabLayoutMediatorController>().toClass<TabLayoutMediatorController>().singleton()

        bind<ArticlesFlowAdapter.Factory>().toClass<ArticlesFlowAdapter.Factory>()
    }
}