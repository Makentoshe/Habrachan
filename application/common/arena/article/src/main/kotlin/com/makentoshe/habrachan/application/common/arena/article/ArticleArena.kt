package com.makentoshe.habrachan.application.common.arena.article

import com.makentoshe.habrachan.application.common.arena.Arena
import com.makentoshe.habrachan.application.common.arena.ArenaCache
import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.manager.GetArticleManager
import com.makentoshe.habrachan.network.request.GetArticleRequest2
import com.makentoshe.habrachan.network.response.GetArticleResponse2
import javax.inject.Inject

abstract class ArticleArena internal constructor(
    private val manager: GetArticleManager<GetArticleRequest2>
) : Arena<GetArticleRequest2, GetArticleResponse2>() {

    abstract fun request(userSession: UserSession, articleId: ArticleId): GetArticleRequest2

    override suspend fun internalSuspendFetch(key: GetArticleRequest2) = manager.article(key)

    class Factory @Inject constructor(
        private val manager: GetArticleManager<GetArticleRequest2>,
        private val arenaStorage: ArenaCache<in GetArticleRequest2, GetArticleResponse2>,
    ) {
        fun sourceFirstArena(): ArticleArena {
            return SourceFirstArticleArena(manager, arenaStorage)
        }

        fun cacheFirstArena(): ArticleArena {
            return CacheFirstArticleArena(manager, arenaStorage)
        }
    }
}

