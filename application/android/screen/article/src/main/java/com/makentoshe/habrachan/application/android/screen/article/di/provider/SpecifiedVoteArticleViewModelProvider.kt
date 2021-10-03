package com.makentoshe.habrachan.application.android.screen.article.di.provider

import com.makentoshe.habrachan.application.android.common.article.voting.viewmodel.VoteArticleViewModel
import com.makentoshe.habrachan.application.android.common.article.voting.viewmodel.VoteArticleViewModelProvider
import com.makentoshe.habrachan.application.android.database.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.screen.article.ArticleFragment
import com.makentoshe.habrachan.application.common.article.voting.VoteArticleArena
import com.makentoshe.habrachan.network.UserSession
import javax.inject.Inject
import javax.inject.Provider

internal class SpecifiedVoteArticleViewModelProvider @Inject constructor(
    private val fragment: ArticleFragment,
    private val userSession: UserSession,
    private val voteArticleArena: VoteArticleArena,
    private val database: AndroidCacheDatabase,
) : Provider<VoteArticleViewModel> {

    override fun get(): VoteArticleViewModel {
        val factory = VoteArticleViewModel.Factory(userSession, voteArticleArena, database)
        return VoteArticleViewModelProvider(factory).get(fragment)
    }
}