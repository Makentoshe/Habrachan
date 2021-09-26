package com.makentoshe.habrachan.application.android.screen.articles.di

import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.articles.ArticlesFlowFragment
import com.makentoshe.habrachan.application.android.screen.articles.model.ArticlesFlowAdapter
import com.makentoshe.habrachan.application.android.screen.articles.navigation.ArticlesNavigation
import com.makentoshe.habrachan.application.android.screen.articles.navigation.StackRouter
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject
import javax.inject.Qualifier

@Qualifier
annotation class ArticlesFlowScope

class ArticlesFlowModule(fragment: ArticlesFlowFragment) : Module() {

    private val router by inject<StackRouter>()

    init {
        Toothpick.openScopes(ApplicationScope::class).inject(this)

        val flowAdapter = ArticlesFlowAdapter(fragment, fragment.arguments.specs)
        bind<ArticlesFlowAdapter>().toInstance(flowAdapter)

        bind<ArticlesNavigation>().toInstance(ArticlesNavigation(router))
    }
}