package com.makentoshe.habrachan.application.android.screen.article.di

import androidx.lifecycle.ViewModelProviders
import com.makentoshe.habrachan.application.android.arena.ArticleArenaCache
import com.makentoshe.habrachan.application.android.arena.AvatarArenaCache
import com.makentoshe.habrachan.application.android.database.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.article.ArticleFragment
import com.makentoshe.habrachan.application.android.screen.article.navigation.ArticleNavigation
import com.makentoshe.habrachan.application.android.screen.article.viewmodel.ArticleViewModel
import com.makentoshe.habrachan.application.android.screen.articles.viewmodel.SchedulersProvider
import com.makentoshe.habrachan.application.core.arena.articles.ArticleArena
import com.makentoshe.habrachan.application.core.arena.image.AvatarArena
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.manager.ArticlesManager
import com.makentoshe.habrachan.network.manager.ImageManager
import io.reactivex.disposables.CompositeDisposable
import okhttp3.OkHttpClient
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

annotation class ArticleScope

class ArticleModule(fragment: ArticleFragment) : Module() {

    private val router by inject<Router>()
    private val client by inject<OkHttpClient>()
    private val session by inject<UserSession>()
    private val schedulersProvider by inject<SchedulersProvider>()

    private val cacheDatabase by inject<AndroidCacheDatabase>()

    init {
        Toothpick.openScope(ApplicationScope::class).inject(this)
        bind<CompositeDisposable>().toInstance(CompositeDisposable())
        bind<ArticleNavigation>().toInstance(ArticleNavigation(router))

        val avatarCache = AvatarArenaCache(cacheDatabase.avatarDao(), fragment.requireContext().cacheDir)
        val avatarArena = AvatarArena(ImageManager.Builder(client).build(), avatarCache)
        val articleCache = ArticleArenaCache(cacheDatabase.articlesSearchDao())
        val articleArena = ArticleArena(ArticlesManager.Builder(client).native(), articleCache)
        val viewModelFactory = ArticleViewModel.Factory(
            CompositeDisposable(), session, articleArena, avatarArena, fragment.arguments, schedulersProvider
        )
        val viewModel = ViewModelProviders.of(fragment, viewModelFactory)[ArticleViewModel::class.java]
        bind<ArticleViewModel>().toInstance(viewModel)
    }
}