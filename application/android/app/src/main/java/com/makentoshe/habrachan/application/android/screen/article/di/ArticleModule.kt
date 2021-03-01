package com.makentoshe.habrachan.application.android.screen.article.di

import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.makentoshe.habrachan.application.android.arena.ArticleArenaCache
import com.makentoshe.habrachan.application.android.arena.AvatarArenaCache
import com.makentoshe.habrachan.application.android.database.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.navigation.StackRouter
import com.makentoshe.habrachan.application.android.screen.article.ArticleFragment
import com.makentoshe.habrachan.application.android.screen.article.model.ArticleHtmlController
import com.makentoshe.habrachan.application.android.screen.article.model.ArticleShareController
import com.makentoshe.habrachan.application.android.screen.article.model.JavaScriptInterface
import com.makentoshe.habrachan.application.android.screen.article.navigation.ArticleNavigation
import com.makentoshe.habrachan.application.android.screen.article.viewmodel.ArticleViewModel2
import com.makentoshe.habrachan.application.core.arena.articles.ArticleArena
import com.makentoshe.habrachan.application.core.arena.image.ImageArena
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.manager.ArticlesManager
import com.makentoshe.habrachan.network.manager.ImageManager
import okhttp3.OkHttpClient
import ru.terrakok.cicerone.Router
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

annotation class ArticleScope

class ArticleModule(fragment: ArticleFragment) : Module() {

    private val router by inject<StackRouter>()
    private val client by inject<OkHttpClient>()
    private val session by inject<UserSession>()

    private val cacheDatabase by inject<AndroidCacheDatabase>()

    init {
        Toothpick.openScope(ApplicationScope::class).inject(this)
        bind<ArticleNavigation>().toInstance(ArticleNavigation(router))

        val avatarCache = AvatarArenaCache(cacheDatabase.avatarDao(), fragment.requireContext().cacheDir)
        val avatarArena = ImageArena(ImageManager.Builder(client).build(), avatarCache)

        val articleCache = ArticleArenaCache(cacheDatabase)
        val articleArena = ArticleArena(ArticlesManager.Builder(client).native(), articleCache)

        val viewModelFactory2 = ArticleViewModel2.Factory(session, articleArena, avatarArena)
        val viewModel2 = ViewModelProviders.of(fragment, viewModelFactory2)[ArticleViewModel2::class.java]
        bind<ArticleViewModel2>().toInstance(viewModel2)

        val articleShareController = ArticleShareController(fragment.arguments.articleId)
        bind<ArticleShareController>().toInstance(articleShareController)

        bind<ArticleHtmlController>().toInstance(ArticleHtmlController(fragment.resources))
        bind<JavaScriptInterface>().toInstance(JavaScriptInterface(fragment.lifecycleScope))
    }
}