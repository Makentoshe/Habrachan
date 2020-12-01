package com.makentoshe.habrachan.application.core.arena.articles

import com.makentoshe.habrachan.application.core.arena.ArenaCache
import com.makentoshe.habrachan.application.core.arena.SourceFirstArena
import com.makentoshe.habrachan.network.manager.ArticlesManager
import com.makentoshe.habrachan.network.request.GetArticlesRequest
import com.makentoshe.habrachan.network.response.ArticlesResponse

class ArticlesArena(
    private val articlesManager: ArticlesManager,
    articlesCache: ArenaCache<GetArticlesRequest, ArticlesResponse>
): SourceFirstArena<GetArticlesRequest, ArticlesResponse>(articlesCache) {

    override suspend fun internalSuspendFetch(key: GetArticlesRequest): Result<ArticlesResponse> {
        return articlesManager.getArticles(key)
    }
}

