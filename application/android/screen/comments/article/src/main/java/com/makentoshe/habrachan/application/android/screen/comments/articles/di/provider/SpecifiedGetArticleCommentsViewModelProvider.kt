package com.makentoshe.habrachan.application.android.screen.comments.articles.di.provider

import com.makentoshe.habrachan.application.android.common.comment.viewmodel.GetArticleCommentsViewModel
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.GetArticleCommentsViewModelProvider
import com.makentoshe.habrachan.application.android.common.usersession.AndroidUserSessionProvider
import com.makentoshe.habrachan.application.android.screen.comments.articles.ArticleCommentsFragment
import com.makentoshe.habrachan.application.common.arena.comments.ArticleCommentsArena
import com.makentoshe.habrachan.functional.Option
import javax.inject.Inject
import javax.inject.Provider

internal class SpecifiedGetArticleCommentsViewModelProvider @Inject constructor(
    private val fragment: ArticleCommentsFragment,
    private val userSessionProvider: AndroidUserSessionProvider,
    private val articleCommentsArena: ArticleCommentsArena,
) : Provider<GetArticleCommentsViewModel> {

    private val initialGetArticleCommentsSpecOption = Option.None

    override fun get(): GetArticleCommentsViewModel {
        val factory = GetArticleCommentsViewModel.Factory(userSessionProvider, articleCommentsArena, initialGetArticleCommentsSpecOption)
        return GetArticleCommentsViewModelProvider(factory).get(fragment)
    }
}