package com.makentoshe.habrachan.network.request

import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.network.UserSession

interface GetCommentsRequest2 {
    val session: UserSession
    val articleId: ArticleId
}