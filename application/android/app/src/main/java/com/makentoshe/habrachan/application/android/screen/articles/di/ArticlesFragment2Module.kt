package com.makentoshe.habrachan.application.android.screen.articles.di

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.application.android.ExceptionHandler
import com.makentoshe.habrachan.application.android.arena.ArticlesArenaCache
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.articles.ArticlesFragment2
import com.makentoshe.habrachan.application.android.screen.articles.model.*
import com.makentoshe.habrachan.application.android.screen.articles.viewmodel.ArticlesViewModel
import com.makentoshe.habrachan.application.android.screen.articles.viewmodel.ExecutorsProvider
import com.makentoshe.habrachan.application.android.screen.articles.viewmodel.SchedulersProvider
import com.makentoshe.habrachan.application.core.arena.articles.ArticlesArena
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.manager.ArticlesManager
import io.reactivex.disposables.CompositeDisposable
import okhttp3.OkHttpClient
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject
import javax.inject.Qualifier

@Qualifier
annotation class ArticlesScope

class ArticlesFragment2Module(fragment: ArticlesFragment2) : Module() {

    private val router by inject<Router>()
    private val client by inject<OkHttpClient>()
    private val executorsProvider by inject<ExecutorsProvider>()
    private val schedulersProvider by inject<SchedulersProvider>()
    private val session by inject<UserSession>()
    private val exceptionHandler by inject<ExceptionHandler>()

    init {
        Toothpick.openScopes(ApplicationScope::class).inject(this)
        bind<CompositeDisposable>().toInstance(CompositeDisposable())
//        bind<ExceptionHandler>().toInstance(exceptionHandler)

        val epoxyController = ArticlesPagedListEpoxyController(
            ArticleEpoxyModel.Factory(router), DivideEpoxyModel.Factory(), FooterEpoxyModel.Factory(exceptionHandler)
        )

        val articlesDisposables = CompositeDisposable()
        val articlesArena = ArticlesArena(ArticlesManager.Builder(client).native(), ArticlesArenaCache())
        val dataSourceFactory = ArticlesDataSource.Factory(session, articlesArena, articlesDisposables)

        val viewModelDisposables = CompositeDisposable().apply { add(articlesDisposables) }
        val factory = ArticlesViewModel.Factory(
            viewModelDisposables, session, epoxyController, executorsProvider, schedulersProvider, dataSourceFactory
        )
        val viewModel = ViewModelProviders.of(fragment, factory)[ArticlesViewModel::class.java]
        bind<ArticlesViewModel>().toInstance(viewModel)
    }
}