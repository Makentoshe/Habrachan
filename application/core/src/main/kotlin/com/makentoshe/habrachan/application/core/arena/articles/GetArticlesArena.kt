package com.makentoshe.habrachan.application.core.arena.articles

import com.makentoshe.habrachan.application.core.arena.ArenaCache
import com.makentoshe.habrachan.application.core.arena.SourceFirstArena
import com.makentoshe.habrachan.functional.fold
import com.makentoshe.habrachan.network.manager.GetArticlesManager
import com.makentoshe.habrachan.network.request.GetArticlesRequest2
import com.makentoshe.habrachan.network.request.GetArticlesSpec
import com.makentoshe.habrachan.network.response.GetArticlesResponse2

class GetArticlesArena(
    val manager: GetArticlesManager<GetArticlesRequest2, GetArticlesSpec>,
    articlesCache: ArenaCache<GetArticlesRequest2, GetArticlesResponse2>
) : SourceFirstArena<GetArticlesRequest2, GetArticlesResponse2>(articlesCache) {
    override suspend fun internalSuspendFetch(key: GetArticlesRequest2): Result<GetArticlesResponse2> {
        return manager.articles(key).fold({
            kotlin.Result.success(it)
        }, {
            kotlin.Result.failure(it)
        })
    }
}