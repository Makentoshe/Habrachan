package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.request.GetArticleRequest2

interface GetArticleManager<Request: GetArticleRequest2> {

    /**
     * Factory method creates request
     *
     * @param articleId
     * @return a request for performing network operation
     * */
    fun request(userSession: UserSession, articleId: ArticleId): Request

    /** Main network method returns article by [request] */
    suspend fun article(request: Request)
}