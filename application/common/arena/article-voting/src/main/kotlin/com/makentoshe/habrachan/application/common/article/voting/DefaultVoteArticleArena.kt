package com.makentoshe.habrachan.application.common.article.voting

import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.manager.VoteArticleManager
import com.makentoshe.habrachan.network.request.ArticleVote
import com.makentoshe.habrachan.network.request.VoteArticleRequest

internal class DefaultVoteArticleArena(
    private val manager: VoteArticleManager<VoteArticleRequest>,
) : VoteArticleArena(manager) {

    override fun request(userSession: UserSession, articleId: ArticleId, articleVote: ArticleVote): VoteArticleRequest {
        return manager.request(userSession, articleId, articleVote)
    }

    override suspend fun suspendCarry(key: VoteArticleRequest) = internalSuspendCarry(key)
}