package com.makentoshe.habrachan.application.core.arena.comments

import com.makentoshe.habrachan.application.core.arena.ArenaCache
import com.makentoshe.habrachan.application.core.arena.SourceFirstArena
import com.makentoshe.habrachan.entity.natives.Comment
import com.makentoshe.habrachan.network.manager.GetArticleCommentsManager
import com.makentoshe.habrachan.network.request.GetArticleCommentsRequest

class CommentsSourceFirstArena(
    val articleCommentsManager: GetArticleCommentsManager<GetArticleCommentsRequest>,
    cache: ArenaCache<GetArticleCommentsRequest, List<Comment>>
) : SourceFirstArena<GetArticleCommentsRequest, List<Comment>>(cache) {

    override suspend fun internalSuspendFetch(key: GetArticleCommentsRequest): Result<List<Comment>> {
        return articleCommentsManager.comments(key).fold({ Result.success(it.data) }, { Result.failure(it) })
    }
}

