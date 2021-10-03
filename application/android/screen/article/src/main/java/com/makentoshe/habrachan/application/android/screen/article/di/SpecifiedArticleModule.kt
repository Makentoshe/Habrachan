package com.makentoshe.habrachan.application.android.screen.article.di

import android.content.res.Resources
import androidx.lifecycle.lifecycleScope
import com.makentoshe.habrachan.application.android.common.article.viewmodel.GetArticleViewModel
import com.makentoshe.habrachan.application.android.common.article.voting.viewmodel.VoteArticleViewModel
import com.makentoshe.habrachan.application.android.common.avatar.viewmodel.GetAvatarViewModel
import com.makentoshe.habrachan.application.android.common.avatar.viewmodel.GetAvatarViewModelProvider
import com.makentoshe.habrachan.application.android.di.ApplicationScope
import com.makentoshe.habrachan.application.android.exception.ExceptionHandler
import com.makentoshe.habrachan.application.android.screen.article.ArticleFragment
import com.makentoshe.habrachan.application.android.screen.article.di.provider.SpecifiedGetArticleViewModelProvider
import com.makentoshe.habrachan.application.android.screen.article.di.provider.SpecifiedVoteArticleViewModelProvider
import com.makentoshe.habrachan.application.common.arena.article.ArticleArena
import com.makentoshe.habrachan.application.common.article.voting.VoteArticleArena
import com.makentoshe.habrachan.entity.ArticleId
import kotlinx.coroutines.CoroutineScope
import toothpick.Toothpick
import toothpick.config.Module
import toothpick.ktp.binding.bind
import toothpick.ktp.delegate.inject

/**
 * Module for a specific [ArticleFragment] provided in constructor.
 *
 * For each [ArticleFragment] instance new scope opens and this module injects.
 * */
class SpecifiedArticleModule(fragment: ArticleFragment) : Module() {

    private val getAvatarViewModelProvider by inject<GetAvatarViewModelProvider>()

    private val articleArenaFactory by inject<ArticleArena.Factory>()
    private val voteArticleArenaFactory by inject<VoteArticleArena.Factory>()

    init {
        Toothpick.openScopes(ApplicationScope::class, ArticleScope::class).inject(this)
        bind<ArticleId>().toInstance(fragment.arguments.articleId)
        bind<CoroutineScope>().toInstance(fragment.lifecycleScope)
        bind<Resources>().toInstance(fragment.resources)
        bind<ArticleFragment>().toInstance(fragment)

        bind<GetAvatarViewModel>().toInstance(getAvatarViewModelProvider.get(fragment))
        bind<ExceptionHandler>().toInstance(ExceptionHandler(fragment.requireContext()))

        bind<ArticleArena>().toInstance(articleArenaFactory.sourceFirstArena())
        bind<GetArticleViewModel>().toProvider(SpecifiedGetArticleViewModelProvider::class).providesSingleton()

        bind<VoteArticleArena>().toInstance(voteArticleArenaFactory.default())
        bind<VoteArticleViewModel>().toProvider(SpecifiedVoteArticleViewModelProvider::class).providesSingleton()
    }
}