package com.makentoshe.habrachan.network.request

import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.network.UserSession

data class MobileGetArticleRequest(
    override val userSession: UserSession, override val articleId: ArticleId
) : MobileRequest(), GetArticleRequest2