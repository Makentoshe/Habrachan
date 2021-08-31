package com.makentoshe.habrachan.application.android.screen.comments.dispatch.di.provider

import com.makentoshe.habrachan.application.android.common.article.viewmodel.GetArticleSpec
import com.makentoshe.habrachan.application.android.common.article.viewmodel.GetArticleViewModel
import com.makentoshe.habrachan.application.android.common.article.viewmodel.GetArticleViewModelProvider
import com.makentoshe.habrachan.application.android.screen.comments.dispatch.DispatchCommentsFragment
import com.makentoshe.habrachan.application.common.arena.article.ArticleArena
import com.makentoshe.habrachan.functional.Option
import com.makentoshe.habrachan.network.UserSession
import javax.inject.Inject
import javax.inject.Provider

internal class SpecifiedGetArticleViewModelProvider @Inject constructor(
    private val fragment: DispatchCommentsFragment,
    private val userSession: UserSession,
    private val articleArena: ArticleArena,
): Provider<GetArticleViewModel> {

    private val initialGetArticleSpecOption = Option.Value(
        GetArticleSpec(fragment.arguments.articleId)
    )

    override fun get(): GetArticleViewModel {
        val factory = GetArticleViewModel.Factory(userSession, articleArena, initialGetArticleSpecOption)
        return GetArticleViewModelProvider(factory).get(fragment)
    }
}