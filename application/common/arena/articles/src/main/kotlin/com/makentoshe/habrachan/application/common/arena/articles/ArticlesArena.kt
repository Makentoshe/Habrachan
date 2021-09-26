package com.makentoshe.habrachan.application.common.arena.articles

import com.makentoshe.habrachan.application.common.arena.Arena
import com.makentoshe.habrachan.application.common.arena.ArenaCache
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.manager.GetArticlesManager
import com.makentoshe.habrachan.network.request.GetArticlesRequest2
import com.makentoshe.habrachan.network.request.GetArticlesSpec
import com.makentoshe.habrachan.network.response.GetArticlesResponse2
import javax.inject.Inject

abstract class ArticlesArena internal constructor(
    private val manager: GetArticlesManager<GetArticlesRequest2, GetArticlesSpec>
) : Arena<GetArticlesRequest2, GetArticlesResponse2>() {

    fun request(userSession: UserSession, page: Int, spec: GetArticlesSpec): GetArticlesRequest2 {
        return manager.request(userSession, page, spec)
    }

    override suspend fun internalSuspendFetch(key: GetArticlesRequest2) = manager.articles(key)

    class Factory @Inject constructor(
        private val manager: GetArticlesManager<GetArticlesRequest2, GetArticlesSpec>,
        private val arenaStorage: ArenaCache<in GetArticlesRequest2, GetArticlesResponse2>,
    ) {
        fun sourceFirstArena(): ArticlesArena {
            return SourceFirstArticleArena(manager, arenaStorage)
        }

        fun cacheFirstArena(): ArticlesArena {
            return CacheFirstArticleArena(manager, arenaStorage)
        }
    }
}

