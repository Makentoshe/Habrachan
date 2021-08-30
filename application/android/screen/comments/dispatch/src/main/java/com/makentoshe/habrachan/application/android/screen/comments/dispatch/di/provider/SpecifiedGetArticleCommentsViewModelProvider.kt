package com.makentoshe.habrachan.application.android.screen.comments.dispatch.di.provider

import com.makentoshe.habrachan.application.android.common.comment.viewmodel.GetArticleCommentsSpec2
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.GetArticleCommentsViewModel
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.GetArticleCommentsViewModelProvider
import com.makentoshe.habrachan.application.android.screen.comments.dispatch.DispatchCommentsFragment
import com.makentoshe.habrachan.application.common.arena.comments.ArticleCommentsArena
import com.makentoshe.habrachan.functional.Option
import com.makentoshe.habrachan.network.UserSession
import javax.inject.Inject
import javax.inject.Provider

internal class SpecifiedGetArticleCommentsViewModelProvider @Inject constructor(
    private val fragment: DispatchCommentsFragment,
    private val userSession: UserSession,
    private val articleCommentsArena: ArticleCommentsArena,
) : Provider<GetArticleCommentsViewModel> {

    private val initialGetArticleCommentsSpecOption = Option.Value(
        GetArticleCommentsSpec2.DispatchCommentsSpec(fragment.arguments.articleId, fragment.arguments.commentId)
    )

    override fun get(): GetArticleCommentsViewModel {
        val factory = GetArticleCommentsViewModel.Factory(userSession, articleCommentsArena, initialGetArticleCommentsSpecOption)
        return GetArticleCommentsViewModelProvider(factory).get(fragment)
    }
}