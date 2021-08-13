package com.makentoshe.habrachan.network

import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.network.request.GetArticleCommentsRequest
import com.makentoshe.habrachan.network.request.NativeRequest

data class NativeGetArticleCommentsRequest(
    override val session: UserSession, override val articleId: ArticleId
) : NativeRequest(), GetArticleCommentsRequest