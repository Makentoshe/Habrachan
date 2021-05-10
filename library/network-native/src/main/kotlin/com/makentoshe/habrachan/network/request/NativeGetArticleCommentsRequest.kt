package com.makentoshe.habrachan.network.request

import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.network.UserSession

data class NativeGetArticleCommentsRequest(
    override val session: UserSession, override val articleId: ArticleId
) : NativeRequest(), GetArticleCommentsRequest