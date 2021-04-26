package com.makentoshe.habrachan.network.request

import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.network.UserSession

/** Request all comments for article by its [articleId] */
data class GetCommentsRequest(val session: UserSession, val articleId: Int, val since: Int = -1)

interface GetCommentsRequest2 {
    val session: UserSession
    val articleId: ArticleId
}