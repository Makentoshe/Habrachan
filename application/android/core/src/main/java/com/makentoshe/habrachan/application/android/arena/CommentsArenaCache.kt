package com.makentoshe.habrachan.application.android.arena

import android.util.Log
import com.makentoshe.habrachan.application.android.BuildConfig
import com.makentoshe.habrachan.application.core.arena.ArenaCache
import com.makentoshe.habrachan.application.core.arena.ArenaStorageException
import com.makentoshe.habrachan.entity.Comment
import com.makentoshe.habrachan.network.request.GetCommentsRequest

class CommentsArenaCache: ArenaCache<GetCommentsRequest, List<Comment>> {

    companion object {
        fun capture(level: Int, message: () -> String) {
            if (!BuildConfig.DEBUG) return
            Log.println(level, "CommentsArenaCache", message())
        }
    }

    override fun fetch(key: GetCommentsRequest): Result<List<Comment>> {
        capture(Log.INFO) { "fetch not implemented" }
        return Result.failure(ArenaStorageException("fetch not implemented"))
    }

    override fun carry(key: GetCommentsRequest, value: List<Comment>) {
        capture(Log.INFO) { "carry not implemented" }
    }
}