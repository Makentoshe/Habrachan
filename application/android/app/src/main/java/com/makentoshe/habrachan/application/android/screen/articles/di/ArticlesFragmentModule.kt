package com.makentoshe.habrachan.application.android.screen.articles.di

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.application.android.arena.ArticlesArenaCache
import com.makentoshe.habrachan.application.android.database.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.articles.ArticlesFragment
import com.makentoshe.habrachan.application.android.screen.articles.viewmodel.ArticlesViewModel2
import com.makentoshe.habrachan.application.core.arena.articles.ArticlesArena
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.manager.ArticlesManager
import okhttp3.OkHttpClient
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject
import javax.inject.Qualifier

@Qualifier
annotation class ArticlesScope

class ArticlesFragmentModule(fragment: ArticlesFragment) : Module() {

    private val router by inject<Router>()
    private val client by inject<OkHttpClient>()
    private val session by inject<UserSession>()
    private val androidCacheDatabase by inject<AndroidCacheDatabase>()

    init {
        Toothpick.openScopes(ApplicationScope::class).inject(this)

        val articlesArenaCache = ArticlesArenaCache(androidCacheDatabase)
        val articlesArena = ArticlesArena(ArticlesManager.Builder(client).native(), articlesArenaCache)

        val factory = ArticlesViewModel2.Factory(session, articlesArena)
        val viewModel = ViewModelProviders.of(fragment, factory)[ArticlesViewModel2::class.java]
        bind<ArticlesViewModel2>().toInstance(viewModel)

//        val epoxyController = ArticlesPagedListEpoxyController(
//            ArticleEpoxyModel.Factory(router), DivideEpoxyModel.Factory(), FooterEpoxyModel.Factory(exceptionHandler)
//        )
//
//        val articlesDisposables = CompositeDisposable()
//        val dataSourceFactory = ArticlesDataSource.Factory(session, articlesArena, articlesDisposables)
//
//        val viewModelDisposables = CompositeDisposable().apply { add(articlesDisposables) }
//        val factory = ArticlesViewModel.Factory(
//            viewModelDisposables, epoxyController, executorsProvider, schedulersProvider, dataSourceFactory
//        )
//        val viewModel = ViewModelProviders.of(fragment, factory)[ArticlesViewModel::class.java]
//        bind<ArticlesViewModel>().toInstance(viewModel)
    }
}