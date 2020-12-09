package com.makentoshe.habrachan.application.core.arena.articles

import com.makentoshe.habrachan.application.core.arena.ArenaCache
import com.makentoshe.habrachan.application.core.arena.SourceFirstArena
import com.makentoshe.habrachan.network.manager.ArticlesManager
import com.makentoshe.habrachan.network.request.GetArticleRequest
import com.makentoshe.habrachan.network.response.ArticleResponse

class ArticleArena(
    private val articlesManager: ArticlesManager,
    articlesCache: ArenaCache<GetArticleRequest, ArticleResponse>
): SourceFirstArena<GetArticleRequest, ArticleResponse>(articlesCache) {

    override suspend fun internalSuspendFetch(key: GetArticleRequest): Result<ArticleResponse> {
        return articlesManager.getArticle(key)
    }
}
