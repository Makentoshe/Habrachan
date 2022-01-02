package com.makentoshe.habrachan.application.android.screen.article.di.module

import androidx.lifecycle.lifecycleScope
import com.makentoshe.habrachan.application.android.common.article.viewmodel.GetArticleViewModel
import com.makentoshe.habrachan.application.android.common.article.viewmodel.GetArticleViewModelProvider
import com.makentoshe.habrachan.application.android.common.article.viewmodel.GetArticleViewModelRequest
import com.makentoshe.habrachan.application.android.common.article.voting.viewmodel.VoteArticleViewModel
import com.makentoshe.habrachan.application.android.common.article.voting.viewmodel.VoteArticleViewModelProvider
import com.makentoshe.habrachan.application.android.common.avatar.viewmodel.GetAvatarViewModel
import com.makentoshe.habrachan.application.android.common.avatar.viewmodel.GetAvatarViewModelProvider
import com.makentoshe.habrachan.application.android.common.di.FragmentInjector
import com.makentoshe.habrachan.application.android.common.strings.BundledStringsProvider
import com.makentoshe.habrachan.application.android.common.usersession.AndroidUserSessionProvider
import com.makentoshe.habrachan.application.android.database.cache.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.screen.article.ArticleFragment
import com.makentoshe.habrachan.application.android.screen.article.di.ArticleScope
import com.makentoshe.habrachan.application.android.screen.article.model.ArticleHtmlController
import com.makentoshe.habrachan.application.android.screen.article.model.ArticleShareController
import com.makentoshe.habrachan.application.android.screen.article.model.JavaScriptInterface
import com.makentoshe.habrachan.application.common.arena.article.get.GetArticleArena
import com.makentoshe.habrachan.application.common.arena.content.GetContentArena
import com.makentoshe.habrachan.application.common.article.voting.VoteArticleArena
import com.makentoshe.habrachan.functional.Option2
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

class ArticleScreenModule(injectorScope: FragmentInjector.FragmentInjectorScope<ArticleFragment>) : Module() {

    private val stringsProvider by inject<BundledStringsProvider>()
    private val userSessionProvider by inject<AndroidUserSessionProvider>()
    private val database by inject<AndroidCacheDatabase>()

    private val voteArticleArena by inject<VoteArticleArena>()
    private val getArticleArena by inject<GetArticleArena>()
    private val getAvatarArena by inject<GetContentArena>()

    init {
        Toothpick.openScopes(ApplicationScope::class, ArticleScope::class).inject(this)
        bind<ArticleShareController>().toInstance(ArticleShareController(injectorScope.fragment.arguments.articleId))
        bind<ArticleHtmlController>().toInstance(ArticleHtmlController(injectorScope.fragment.resources))
        bind<JavaScriptInterface>().toInstance(JavaScriptInterface(injectorScope.fragment.lifecycleScope))

        bind<VoteArticleViewModel>().toInstance(voteArticleViewModel(injectorScope))
        bind<GetAvatarViewModel>().toInstance(getAvatarViewModel(injectorScope))
        bind<GetArticleViewModel>().toInstance(getArticleViewModel(injectorScope))
    }

    private fun getAvatarViewModel(injectorScope: FragmentInjector.FragmentInjectorScope<ArticleFragment>): GetAvatarViewModel {
        val avatarFactory = GetAvatarViewModel.Factory(stringsProvider, userSessionProvider, getAvatarArena)
        return GetAvatarViewModelProvider(avatarFactory).get(injectorScope.fragment)
    }

    private fun getArticleViewModel(injectorScope: FragmentInjector.FragmentInjectorScope<ArticleFragment>): GetArticleViewModel {
        val articleInitialOption = Option2.from(GetArticleViewModelRequest(injectorScope.fragment.arguments.articleId))
        val articleFactory = GetArticleViewModel.Factory(stringsProvider, userSessionProvider, getArticleArena, articleInitialOption)
        return GetArticleViewModelProvider(articleFactory).get(injectorScope.fragment)
    }

    private fun voteArticleViewModel(injectorScope: FragmentInjector.FragmentInjectorScope<ArticleFragment>): VoteArticleViewModel {
        val voteFactory = VoteArticleViewModel.Factory(userSessionProvider, voteArticleArena, database)
        return VoteArticleViewModelProvider(voteFactory).get(injectorScope.fragment)
    }

}