package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.request.VoteArticleRequest
import com.makentoshe.habrachan.network.response.VoteArticleResponse

interface VoteArticleManager<Request: VoteArticleRequest> {

    /**
     * Factory method creates request
     *
     * @param articleId
     * @return a request for performing network operation
     * */
    fun request(userSession: UserSession, articleId: ArticleId): Request

    /** Main network method returns vote article response by [request] */
    suspend fun article(request: Request): Result<VoteArticleResponse>
}