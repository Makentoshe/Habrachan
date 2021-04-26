package com.makentoshe.habrachan.network.request

import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.network.UserSession

data class NativeGetCommentsRequest(
    override val session: UserSession, override val articleId: ArticleId
) : NativeRequest(), GetCommentsRequest2