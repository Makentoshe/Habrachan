package com.makentoshe.habrachan.network

import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.network.request.ArticleVote
import com.makentoshe.habrachan.network.request.NativeRequest
import com.makentoshe.habrachan.network.request.VoteArticleRequest

data class NativeVoteArticleRequest(
    override val articleId: ArticleId, override val userSession: UserSession, override val articleVote: ArticleVote
) : NativeRequest(), VoteArticleRequest
