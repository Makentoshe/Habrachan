package com.makentoshe.habrachan.application.android.screen.articles.di

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.application.android.ExceptionHandler
import com.makentoshe.habrachan.application.android.arena.GetArticlesArenaCache
import com.makentoshe.habrachan.application.android.database.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.navigation.StackRouter
import com.makentoshe.habrachan.application.android.screen.articles.ArticlesFragment
import com.makentoshe.habrachan.application.android.screen.articles.model.AppendArticleAdapter
import com.makentoshe.habrachan.application.android.screen.articles.model.ArticleAdapter
import com.makentoshe.habrachan.application.android.screen.articles.navigation.ArticlesNavigation
import com.makentoshe.habrachan.application.android.screen.articles.viewmodel.ArticlesViewModel
import com.makentoshe.habrachan.application.core.arena.articles.GetArticlesArena
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.manager.GetArticlesManager
import com.makentoshe.habrachan.network.request.GetArticlesRequest2
import com.makentoshe.habrachan.network.request.GetArticlesSpec
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject
import javax.inject.Qualifier

@Qualifier
annotation class ArticlesScope
class ArticlesFragmentModule(fragment: ArticlesFragment) : Module() {

    private val router by inject<StackRouter>()
    private val session by inject<UserSession>()
    private val exceptionHandler by inject<ExceptionHandler>()
    private val androidCacheDatabase by inject<AndroidCacheDatabase>()

    private val getArticlesManager by inject<GetArticlesManager<GetArticlesRequest2, GetArticlesSpec>>()

    init {
        Toothpick.openScopes(ApplicationScope::class).inject(this)

        val articlesArenaCache = GetArticlesArenaCache(androidCacheDatabase)
        val articlesArena = GetArticlesArena(getArticlesManager, articlesArenaCache)
        val factory = ArticlesViewModel.Factory(session, articlesArena)
        val viewModel = ViewModelProviders.of(fragment, factory)[ArticlesViewModel::class.java]
        bind<ArticlesViewModel>().toInstance(viewModel)

        val adapter = ArticleAdapter(ArticlesNavigation(router))
        bind<ArticleAdapter>().toInstance(adapter)

        val appendAdapter = AppendArticleAdapter(adapter, exceptionHandler)
        bind<AppendArticleAdapter>().toInstance(appendAdapter)
    }
}