package com.makentoshe.habrachan.application.common.arena.comments

import com.makentoshe.habrachan.application.common.arena.ArenaCache
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.manager.GetArticleCommentsManager
import com.makentoshe.habrachan.network.request.GetArticleCommentsRequest
import com.makentoshe.habrachan.network.response.GetArticleCommentsResponse

internal class SourceFirstArticleCommentsArena constructor(
    private val manager: GetArticleCommentsManager<GetArticleCommentsRequest>,
    override val arenaStorage: ArenaCache<in GetArticleCommentsRequest, GetArticleCommentsResponse>,
) : ArticleCommentsArena(manager) {

    override fun request(userSession: UserSession, articleId: Int) = manager.request(userSession, articleId)

    override suspend fun suspendFetch(key: GetArticleCommentsRequest) = sourceFirstSuspendFetch(key)
}