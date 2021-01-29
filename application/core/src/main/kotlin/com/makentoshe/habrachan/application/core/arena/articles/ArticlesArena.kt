package com.makentoshe.habrachan.application.core.arena.articles

import com.makentoshe.habrachan.application.core.arena.ArenaCache
import com.makentoshe.habrachan.application.core.arena.SourceFirstArena
import com.makentoshe.habrachan.network.manager.ArticlesManager
import com.makentoshe.habrachan.network.request.GetArticlesRequest
import com.makentoshe.habrachan.network.response.GetArticlesResponse

class ArticlesArena(
    private val articlesManager: ArticlesManager,
    articlesCache: ArenaCache<GetArticlesRequest, GetArticlesResponse>
): SourceFirstArena<GetArticlesRequest, GetArticlesResponse>(articlesCache) {

    override suspend fun internalSuspendFetch(key: GetArticlesRequest): Result<GetArticlesResponse> {
        return articlesManager.getArticles(key)
    }
}

