package com.makentoshe.habrachan.application.android.screen.comments.dispatch.di.provider

import com.makentoshe.habrachan.application.android.common.article.viewmodel.GetArticleSpec
import com.makentoshe.habrachan.application.android.common.article.viewmodel.GetArticleViewModel
import com.makentoshe.habrachan.application.android.common.article.viewmodel.GetArticleViewModelProvider
import com.makentoshe.habrachan.application.android.common.usersession.AndroidUserSessionProvider
import com.makentoshe.habrachan.application.android.screen.comments.dispatch.DispatchCommentsFragment
import com.makentoshe.habrachan.application.common.arena.article.ArticleArena
import com.makentoshe.habrachan.functional.Option
import javax.inject.Inject
import javax.inject.Provider

internal class SpecifiedGetArticleViewModelProvider @Inject constructor(
    private val fragment: DispatchCommentsFragment,
    private val userSessionProvider: AndroidUserSessionProvider,
    private val articleArena: ArticleArena,
): Provider<GetArticleViewModel> {

    private val initialGetArticleSpecOption = Option.Value(
        GetArticleSpec(fragment.arguments.articleId)
    )

    override fun get(): GetArticleViewModel {
        val factory = GetArticleViewModel.Factory(userSessionProvider, articleArena, initialGetArticleSpecOption)
        return GetArticleViewModelProvider(factory).get(fragment)
    }
}