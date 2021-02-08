package com.makentoshe.habrachan.network.request

import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.network.UserSession

/** Request a full data about article by it's id */
data class GetArticleNativeRequest(
    val session: UserSession, override val articleId: ArticleId
) : NativeRequest(), GetArticleRequest2