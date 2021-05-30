package com.makentoshe.habrachan.network.request

import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.network.UserSession

interface VoteArticleRequest : Request {
    val articleId: ArticleId
    val userSession: UserSession
    val articleVote: ArticleVote
}

enum class ArticleVote {
    UP, DOWN
}
