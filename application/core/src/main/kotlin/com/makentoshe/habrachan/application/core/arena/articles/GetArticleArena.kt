package com.makentoshe.habrachan.application.core.arena.articles

import com.makentoshe.habrachan.application.core.arena.ArenaCache
import com.makentoshe.habrachan.application.core.arena.SourceFirstArena
import com.makentoshe.habrachan.functional.fold
import com.makentoshe.habrachan.network.manager.GetArticleManager
import com.makentoshe.habrachan.network.request.GetArticleRequest2
import com.makentoshe.habrachan.network.response.GetArticleResponse2

class GetArticleArena(
    val manager: GetArticleManager<GetArticleRequest2>,
    articlesCache: ArenaCache<GetArticleRequest2, GetArticleResponse2>
) : SourceFirstArena<GetArticleRequest2, GetArticleResponse2>(articlesCache) {
    override suspend fun internalSuspendFetch(key: GetArticleRequest2): kotlin.Result<GetArticleResponse2> {
        return manager.article(key).fold({ kotlin.Result.success(it) },{ kotlin.Result.failure(it) })
    }
}

