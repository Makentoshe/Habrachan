package com.makentoshe.habrachan.application.common.arena.articles

import com.makentoshe.habrachan.application.common.arena.Arena3
import com.makentoshe.habrachan.application.common.arena.ArenaCache3
import com.makentoshe.habrachan.network.articles.get.GetArticlesManager
import com.makentoshe.habrachan.network.articles.get.GetArticlesRequest
import com.makentoshe.habrachan.network.articles.get.GetArticlesResponse
import javax.inject.Inject

abstract class ArticlesArena3 internal constructor(
    private val manager: GetArticlesManager
) : Arena3<GetArticlesRequest, GetArticlesResponse>() {

    override suspend fun internalSuspendFetch(key: GetArticlesRequest) = manager.execute(key)

    class Factory @Inject constructor(
        private val manager: GetArticlesManager,
        private val arenaStorage: ArenaCache3<in GetArticlesRequest, GetArticlesResponse>,
    ) {
        fun sourceFirstArena(): ArticlesArena3 {
            return SourceFirstArticleArena3(manager, arenaStorage)
        }

        fun cacheFirstArena(): ArticlesArena3 {
            return CacheFirstArticleArena3(manager, arenaStorage)
        }
    }
}

internal class SourceFirstArticleArena3 constructor(
    manager: GetArticlesManager,
    override val arenaStorage: ArenaCache3<in GetArticlesRequest, GetArticlesResponse>,
) : ArticlesArena3(manager) {

    override suspend fun suspendFetch(key: GetArticlesRequest) = sourceFirstSuspendFetch(key)
}

internal class CacheFirstArticleArena3 constructor(
    manager: GetArticlesManager,
    override val arenaStorage: ArenaCache3<in GetArticlesRequest, GetArticlesResponse>,
) : ArticlesArena3(manager) {

    override suspend fun suspendFetch(key: GetArticlesRequest) = cacheFirstSuspendFetch(key)
}
