package com.makentoshe.habrachan.network.request

import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.network.UserSession

interface GetArticleCommentsRequest {
    val session: UserSession
    val articleId: ArticleId
}