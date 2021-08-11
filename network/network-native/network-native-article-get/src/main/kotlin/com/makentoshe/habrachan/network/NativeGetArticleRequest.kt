package com.makentoshe.habrachan.network

import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.network.request.GetArticleRequest2
import com.makentoshe.habrachan.network.request.NativeRequest

/** Request a full data about article by it's id */
data class NativeGetArticleRequest(
    override val userSession: UserSession,
    override val articleId: ArticleId
) : NativeRequest(), GetArticleRequest2