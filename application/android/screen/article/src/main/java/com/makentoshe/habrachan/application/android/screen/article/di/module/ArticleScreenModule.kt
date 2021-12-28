package com.makentoshe.habrachan.application.android.screen.article.di.module

import androidx.lifecycle.lifecycleScope
import com.makentoshe.habrachan.application.android.common.article.viewmodel.GetArticleSpec
import com.makentoshe.habrachan.application.android.common.article.viewmodel.GetArticleViewModel
import com.makentoshe.habrachan.application.android.common.article.viewmodel.GetArticleViewModelProvider
import com.makentoshe.habrachan.application.android.common.article.voting.viewmodel.VoteArticleViewModel
import com.makentoshe.habrachan.application.android.common.article.voting.viewmodel.VoteArticleViewModelProvider
import com.makentoshe.habrachan.application.android.common.di.FragmentInjector
import com.makentoshe.habrachan.application.android.common.usersession.AndroidUserSessionProvider
import com.makentoshe.habrachan.application.android.database.cache.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.article.ArticleFragment
import com.makentoshe.habrachan.application.android.screen.article.di.ArticleScope
import com.makentoshe.habrachan.application.android.screen.article.model.ArticleHtmlController
import com.makentoshe.habrachan.application.android.screen.article.model.ArticleShareController
import com.makentoshe.habrachan.application.android.screen.article.model.JavaScriptInterface
import com.makentoshe.habrachan.application.common.arena.article.ArticleArena
import com.makentoshe.habrachan.application.common.article.voting.VoteArticleArena
import com.makentoshe.habrachan.functional.Option
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class ArticleScreenModule(injectorScope: FragmentInjector.FragmentInjectorScope<ArticleFragment>) : Module() {

    private val userSessionProvider by inject<AndroidUserSessionProvider>()
    private val database by inject<AndroidCacheDatabase>()

    private val voteArticleArena by inject<VoteArticleArena>()
    private val getArticleArena by inject<ArticleArena>()

    init {
        Toothpick.openScopes(ApplicationScope::class, ArticleScope::class).inject(this)
        bind<ArticleShareController>().toInstance(ArticleShareController(injectorScope.fragment.arguments.articleId))
        bind<ArticleHtmlController>().toInstance(ArticleHtmlController(injectorScope.fragment.resources))
        bind<JavaScriptInterface>().toInstance(JavaScriptInterface(injectorScope.fragment.lifecycleScope))

        val voteFactory = VoteArticleViewModel.Factory(userSessionProvider, voteArticleArena, database)
        bind<VoteArticleViewModel>().toInstance(VoteArticleViewModelProvider(voteFactory).get(injectorScope.fragment))

        val articleInitialOption = Option.from(GetArticleSpec(injectorScope.fragment.arguments.articleId))
        val articleFactory = GetArticleViewModel.Factory(userSessionProvider, getArticleArena, articleInitialOption)
        bind<GetArticleViewModel>().toInstance(GetArticleViewModelProvider(articleFactory).get(injectorScope.fragment))
    }

}