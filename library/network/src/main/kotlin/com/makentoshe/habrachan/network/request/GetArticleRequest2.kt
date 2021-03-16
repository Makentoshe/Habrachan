package com.makentoshe.habrachan.network.request

import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.network.UserSession

interface GetArticleRequest2: Request {
    val articleId: ArticleId
    val userSession: UserSession
}
