package com.makentoshe.habrachan.application.core.arena.comments

import com.makentoshe.habrachan.application.core.arena.ArenaCache
import com.makentoshe.habrachan.application.core.arena.SourceFirstArena
import com.makentoshe.habrachan.entity.natives.Comment
import com.makentoshe.habrachan.network.manager.GetCommentsManager
import com.makentoshe.habrachan.network.request.GetCommentsRequest2

class CommentsSourceFirstArena(
    val commentsManager: GetCommentsManager<GetCommentsRequest2>,
    cache: ArenaCache<GetCommentsRequest2, List<Comment>>
) : SourceFirstArena<GetCommentsRequest2, List<Comment>>(cache) {

    override suspend fun internalSuspendFetch(key: GetCommentsRequest2): Result<List<Comment>> {
        return commentsManager.comments(key).fold({ Result.success(it.data) }, { Result.failure(it) })
    }
}

