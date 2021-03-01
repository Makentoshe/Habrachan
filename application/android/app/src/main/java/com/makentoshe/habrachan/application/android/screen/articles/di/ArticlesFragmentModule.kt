package com.makentoshe.habrachan.application.android.screen.articles.di

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.application.android.ExceptionHandler
import com.makentoshe.habrachan.application.android.arena.ArticlesArenaCache
import com.makentoshe.habrachan.application.android.database.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.navigation.StackRouter
import com.makentoshe.habrachan.application.android.screen.articles.ArticlesFragment
import com.makentoshe.habrachan.application.android.screen.articles.model.AppendArticleAdapter
import com.makentoshe.habrachan.application.android.screen.articles.model.ArticleAdapter
import com.makentoshe.habrachan.application.android.screen.articles.navigation.ArticlesNavigation
import com.makentoshe.habrachan.application.android.screen.articles.viewmodel.ArticlesViewModel
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

    private val router by inject<StackRouter>()
    private val client by inject<OkHttpClient>()
    private val session by inject<UserSession>()
    private val exceptionHandler by inject<ExceptionHandler>()
    private val androidCacheDatabase by inject<AndroidCacheDatabase>()

    init {
        Toothpick.openScopes(ApplicationScope::class).inject(this)

        val articlesArenaCache = ArticlesArenaCache(androidCacheDatabase)
        val articlesArena = ArticlesArena(ArticlesManager.Builder(client).native(), articlesArenaCache)
        val factory = ArticlesViewModel.Factory(session, articlesArena)
        val viewModel = ViewModelProviders.of(fragment, factory)[ArticlesViewModel::class.java]
        bind<ArticlesViewModel>().toInstance(viewModel)

        val adapter = ArticleAdapter(ArticlesNavigation(router))
        bind<ArticleAdapter>().toInstance(adapter)

        val appendAdapter = AppendArticleAdapter(adapter, exceptionHandler)
        bind<AppendArticleAdapter>().toInstance(appendAdapter)
    }
}