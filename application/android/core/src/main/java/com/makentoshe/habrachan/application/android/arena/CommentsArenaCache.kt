package com.makentoshe.habrachan.application.android.arena

import com.makentoshe.habrachan.application.core.arena.ArenaCache
import com.makentoshe.habrachan.application.core.arena.ArenaStorageException
import com.makentoshe.habrachan.entity.Comment
import com.makentoshe.habrachan.network.request.GetCommentsRequest

class CommentsArenaCache: ArenaCache<GetCommentsRequest, List<Comment>> {

    override fun fetch(key: GetCommentsRequest): Result<List<Comment>> {
        return Result.failure(ArenaStorageException("fetch not implemented"))
    }

    override fun carry(key: GetCommentsRequest, value: List<Comment>) {

    }
}