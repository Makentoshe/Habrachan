package com.makentoshe.habrachan.application.common.arena.article

import com.makentoshe.habrachan.application.common.arena.ArenaCache
import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.manager.GetArticleManager
import com.makentoshe.habrachan.network.request.GetArticleRequest2
import com.makentoshe.habrachan.network.response.GetArticleResponse2

internal class SourceFirstArticleArena constructor(
    private val manager: GetArticleManager<GetArticleRequest2>,
    override val arenaStorage: ArenaCache<in GetArticleRequest2, GetArticleResponse2>,
) : ArticleArena(manager) {

    override fun request(userSession: UserSession, articleId: ArticleId) = manager.request(userSession, articleId)

    override suspend fun suspendFetch(key: GetArticleRequest2) = sourceFirstSuspendFetch(key)
}