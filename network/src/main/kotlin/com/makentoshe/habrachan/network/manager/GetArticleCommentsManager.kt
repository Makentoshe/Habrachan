package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.request.GetArticleCommentsRequest
import com.makentoshe.habrachan.network.response.GetArticleCommentsResponse

interface GetArticleCommentsManager<Request: GetArticleCommentsRequest> {

    fun request(userSession: UserSession, articleId: Int): Request

    suspend fun comments(request: Request): Result<GetArticleCommentsResponse>
}
