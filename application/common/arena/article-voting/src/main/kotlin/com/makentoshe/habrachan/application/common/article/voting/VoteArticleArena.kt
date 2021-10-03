package com.makentoshe.habrachan.application.common.article.voting

import com.makentoshe.habrachan.application.common.arena.CarryArena
import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.manager.VoteArticleManager
import com.makentoshe.habrachan.network.request.ArticleVote
import com.makentoshe.habrachan.network.request.VoteArticleRequest
import com.makentoshe.habrachan.network.response.VoteArticleResponse
import javax.inject.Inject

abstract class VoteArticleArena(
    private val manager: VoteArticleManager<VoteArticleRequest>
) : CarryArena<VoteArticleRequest, VoteArticleResponse>() {

    abstract fun request(userSession: UserSession, articleId: ArticleId, articleVote: ArticleVote): VoteArticleRequest

    override suspend fun internalSuspendCarry(key: VoteArticleRequest) = manager.vote(key)

    class Factory @Inject constructor(
        private val manager: VoteArticleManager<VoteArticleRequest>
    ) {
        fun default(): VoteArticleArena {
            return DefaultVoteArticleArena(manager)
        }
    }
}


