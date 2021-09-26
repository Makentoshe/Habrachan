package com.makentoshe.habrachan.application.common.arena.articles

import com.makentoshe.habrachan.application.common.arena.ArenaCache
import com.makentoshe.habrachan.network.manager.GetArticlesManager
import com.makentoshe.habrachan.network.request.GetArticlesRequest2
import com.makentoshe.habrachan.network.request.GetArticlesSpec
import com.makentoshe.habrachan.network.response.GetArticlesResponse2

internal class CacheFirstArticleArena constructor(
    manager: GetArticlesManager<GetArticlesRequest2, GetArticlesSpec>,
    override val arenaStorage: ArenaCache<in GetArticlesRequest2, GetArticlesResponse2>,
) : ArticlesArena(manager) {

    override suspend fun suspendFetch(key: GetArticlesRequest2) = cacheFirstSuspendFetch(key)
}