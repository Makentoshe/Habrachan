package com.makentoshe.habrachan.application.core.arena.articles

import com.makentoshe.habrachan.application.core.arena.ArenaCache
import com.makentoshe.habrachan.application.core.arena.SourceFirstArena
import com.makentoshe.habrachan.network.manager.ArticlesManager
import com.makentoshe.habrachan.network.request.GetArticlesRequest
import com.makentoshe.habrachan.network.response.GetArticlesResponse2

class ArticlesArena(
    private val articlesManager: ArticlesManager,
    articlesCache: ArenaCache<GetArticlesRequest, GetArticlesResponse2>
): SourceFirstArena<GetArticlesRequest, GetArticlesResponse2>(articlesCache) {

    override suspend fun internalSuspendFetch(key: GetArticlesRequest): Result<GetArticlesResponse2> {
        return articlesManager.getArticles(key)
    }
}

