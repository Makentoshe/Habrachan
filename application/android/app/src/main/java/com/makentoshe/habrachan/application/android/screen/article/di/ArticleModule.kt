package com.makentoshe.habrachan.application.android.screen.article.di

import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.makentoshe.habrachan.application.android.arena.AvatarArenaCache
import com.makentoshe.habrachan.application.android.arena.GetArticleArenaCache
import com.makentoshe.habrachan.application.android.database.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.navigation.StackRouter
import com.makentoshe.habrachan.application.android.screen.article.ArticleFragment
import com.makentoshe.habrachan.application.android.screen.article.model.ArticleHtmlController
import com.makentoshe.habrachan.application.android.screen.article.model.ArticleShareController
import com.makentoshe.habrachan.application.android.screen.article.model.JavaScriptInterface
import com.makentoshe.habrachan.application.android.screen.article.navigation.ArticleNavigation
import com.makentoshe.habrachan.application.android.screen.article.viewmodel.ArticleViewModel2
import com.makentoshe.habrachan.application.core.arena.articles.GetArticleArena
import com.makentoshe.habrachan.application.core.arena.image.ContentArena
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.manager.GetArticleManager
import com.makentoshe.habrachan.network.manager.GetContentManager
import com.makentoshe.habrachan.network.manager.VoteArticleManager
import com.makentoshe.habrachan.network.request.GetArticleRequest2
import com.makentoshe.habrachan.network.request.VoteArticleRequest
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

annotation class ArticleScope

class ArticleModule(fragment: ArticleFragment) : Module() {

    private val router by inject<StackRouter>()
    private val session by inject<UserSession>()
    private val voteArticleManager by inject<VoteArticleManager<VoteArticleRequest>>()

    private val cacheDatabase by inject<AndroidCacheDatabase>()

    private val getContentManager by inject<GetContentManager>()
    private val getArticleManager by inject<GetArticleManager<GetArticleRequest2>>()

    init {
        Toothpick.openScope(ApplicationScope::class).inject(this)
        bind<ArticleNavigation>().toInstance(ArticleNavigation(router))

        val avatarCache = AvatarArenaCache(cacheDatabase.avatarDao(), fragment.requireContext().cacheDir)
        val avatarArena = ContentArena(getContentManager, avatarCache)

        val articleCache = GetArticleArenaCache(cacheDatabase)
        val articleArena = GetArticleArena(getArticleManager, articleCache)

        val viewModelFactory2 = ArticleViewModel2.Factory(session, articleArena, avatarArena, voteArticleManager)
        val viewModel2 = ViewModelProviders.of(fragment, viewModelFactory2)[ArticleViewModel2::class.java]
        bind<ArticleViewModel2>().toInstance(viewModel2)

        val articleShareController = ArticleShareController(fragment.arguments.articleId)
        bind<ArticleShareController>().toInstance(articleShareController)

        bind<ArticleHtmlController>().toInstance(ArticleHtmlController(fragment.resources))
        bind<JavaScriptInterface>().toInstance(JavaScriptInterface(fragment.lifecycleScope))
    }
}