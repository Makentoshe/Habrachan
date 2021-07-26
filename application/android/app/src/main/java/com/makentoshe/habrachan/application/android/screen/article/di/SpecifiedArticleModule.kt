package com.makentoshe.habrachan.application.android.screen.article.di

import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.makentoshe.habrachan.application.android.common.di.FragmentInjector
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.article.ArticleFragment
import com.makentoshe.habrachan.application.android.screen.article.model.ArticleShareController
import com.makentoshe.habrachan.application.android.screen.article.model.JavaScriptInterface
import com.makentoshe.habrachan.application.android.screen.article.viewmodel.ArticleViewModel2
import com.makentoshe.habrachan.application.core.arena.articles.GetArticleArena
import com.makentoshe.habrachan.application.core.arena.image.ContentArena
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.manager.VoteArticleManager
import com.makentoshe.habrachan.network.request.VoteArticleRequest
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

/** This module is a leaf of the dependency tree */
class SpecifiedArticleModule(injectorScope: FragmentInjector.FragmentInjectorScope<ArticleFragment>) : Module() {

    private val session by inject<UserSession>()
    private val voteArticleManager by inject<VoteArticleManager<VoteArticleRequest>>()
    private val getArticleArena by inject<GetArticleArena>()
    private val getAvatarArena by inject<ContentArena>()

    init {
        Toothpick.openScopes(ApplicationScope::class, ArticleScope::class).inject(this)

        val viewModelFactory2 = ArticleViewModel2.Factory(session, getArticleArena, getAvatarArena, voteArticleManager)
        val viewModel2 = ViewModelProviders.of(injectorScope.fragment, viewModelFactory2)[ArticleViewModel2::class.java]
        bind<ArticleViewModel2>().toInstance(viewModel2)

        bind<ArticleShareController>().toInstance(ArticleShareController(injectorScope.fragment.arguments.articleId))
        bind<JavaScriptInterface>().toInstance(JavaScriptInterface(injectorScope.fragment.lifecycleScope))
    }
}