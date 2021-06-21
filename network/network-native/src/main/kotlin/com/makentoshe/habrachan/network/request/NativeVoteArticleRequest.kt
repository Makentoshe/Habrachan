package com.makentoshe.habrachan.network.request

import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.network.UserSession

data class NativeVoteArticleRequest(
    override val articleId: ArticleId, override val userSession: UserSession, override val articleVote: ArticleVote
) : NativeRequest(), VoteArticleRequest
