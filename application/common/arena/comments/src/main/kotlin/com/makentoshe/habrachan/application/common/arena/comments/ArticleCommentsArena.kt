package com.makentoshe.habrachan.application.common.arena.comments

import com.makentoshe.habrachan.application.common.arena.Arena
import com.makentoshe.habrachan.application.common.arena.ArenaCache
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.manager.GetArticleCommentsManager
import com.makentoshe.habrachan.network.request.GetArticleCommentsRequest
import com.makentoshe.habrachan.network.response.GetArticleCommentsResponse
import javax.inject.Inject

abstract class ArticleCommentsArena internal constructor(
    private val manager: GetArticleCommentsManager<GetArticleCommentsRequest>
) : Arena<GetArticleCommentsRequest, GetArticleCommentsResponse>() {

    abstract fun request(userSession: UserSession, articleId: Int): GetArticleCommentsRequest

    override suspend fun internalSuspendFetch(key: GetArticleCommentsRequest) = manager.comments(key)

    class Factory @Inject constructor(
        private val manager: GetArticleCommentsManager<GetArticleCommentsRequest>,
        private val arenaStorage: ArenaCache<in GetArticleCommentsRequest, GetArticleCommentsResponse>,
    ) {
        fun sourceFirstArena(): ArticleCommentsArena {
            return SourceFirstArticleCommentsArena(manager, arenaStorage)
        }

        fun cacheFirstArena(): ArticleCommentsArena {
            return CacheFirstArticleCommentsArena(manager, arenaStorage)
        }
    }
}
