package com.makentoshe.habrachan.application.android.screen.articles.di

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.application.android.ExceptionHandler
import com.makentoshe.habrachan.application.android.arena.ArticlesArenaCache
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.articles.ArticlesFragment2
import com.makentoshe.habrachan.application.android.screen.articles.model.ArticleEpoxyModel
import com.makentoshe.habrachan.application.android.screen.articles.model.ArticlesPagedListEpoxyController
import com.makentoshe.habrachan.application.android.screen.articles.model.DivideEpoxyModel
import com.makentoshe.habrachan.application.android.screen.articles.model.FooterEpoxyModel
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

        val articlesEpoxyController = ArticlesPagedListEpoxyController(
            ArticleEpoxyModel.Factory(router), DivideEpoxyModel.Factory(), FooterEpoxyModel.Factory(exceptionHandler)
        )

        val articlesManager = ArticlesManager.Builder(client).native()
        val articlesArena = ArticlesArena(articlesManager, ArticlesArenaCache())
        val factory = ArticlesViewModel.Factory(
            CompositeDisposable(),
            session,
            articlesArena,
            articlesEpoxyController,
            executorsProvider,
            schedulersProvider
        )
        val viewModel = ViewModelProviders.of(fragment, factory)[ArticlesViewModel::class.java]
        bind<ArticlesViewModel>().toInstance(viewModel)
    }
}