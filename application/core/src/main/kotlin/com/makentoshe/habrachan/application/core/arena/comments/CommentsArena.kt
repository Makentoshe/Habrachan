package com.makentoshe.habrachan.application.core.arena.comments

import com.makentoshe.habrachan.application.core.arena.ArenaCache
import com.makentoshe.habrachan.application.core.arena.SourceFirstArena
import com.makentoshe.habrachan.entity.Comment
import com.makentoshe.habrachan.network.manager.CommentsManager
import com.makentoshe.habrachan.network.request.GetCommentsRequest

class CommentsArena(
    private val commentsManager: CommentsManager,
    cache: ArenaCache<GetCommentsRequest, List<Comment>>
) : SourceFirstArena<GetCommentsRequest, List<Comment>>(cache) {

    override suspend fun internalSuspendFetch(key: GetCommentsRequest): Result<List<Comment>> {
        return commentsManager.getComments(key).fold({ Result.success(it.data) }, { Result.failure(it) })
    }
}

