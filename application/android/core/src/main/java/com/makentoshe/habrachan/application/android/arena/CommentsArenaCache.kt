package com.makentoshe.habrachan.application.android.arena

import android.util.Log
import com.makentoshe.habrachan.application.android.BuildConfig
import com.makentoshe.habrachan.application.android.database.dao.CommentDao
import com.makentoshe.habrachan.application.android.database.record.CommentRecord
import com.makentoshe.habrachan.application.core.arena.ArenaCache
import com.makentoshe.habrachan.application.core.arena.ArenaStorageException
import com.makentoshe.habrachan.entity.natives.Comment
import com.makentoshe.habrachan.network.request.GetCommentsRequest2

class CommentsArenaCache(
    private val commentDao: CommentDao
) : ArenaCache<GetCommentsRequest2, List<Comment>> {

    companion object {
        private const val limit = 3000
        private const val step = 500

        fun capture(level: Int, message: () -> String) {
            if (!BuildConfig.DEBUG) return
            Log.println(level, "CommentsArenaCache", message())
        }
    }

    override fun fetch(key: GetCommentsRequest2): Result<List<Comment>> = try {
        val records = commentDao.getByArticleId(key.articleId.articleId)
        capture(Log.INFO) { "Fetched ${records.size} comments by key: $key" }
        if (records.isEmpty()) {
            Result.failure(ArenaStorageException("CommentsArenaCache"))
        } else {
            Result.success(records.map { it.toComment() })
        }
    } catch (exception: Exception) {
        capture(Log.INFO) { "Could not fetch comments: $exception"}
        Result.failure(ArenaStorageException("CommentsArenaCache").initCause(exception))
    }

    override fun carry(key: GetCommentsRequest2, value: List<Comment>) {
        if (commentDao.count() > limit) capture(Log.WARN) {
            // TODO implement removing oldest elements
            "TODO: Removing oldest $step elements from cache"
        }

        // TODO if the element already placed in the database - reset it lifecycle index
        capture(Log.INFO) {"Carry ${value.size} comments to cache with key: $key" }
        value.map { comment -> CommentRecord(key.articleId.articleId, comment) }.forEach { record ->
            commentDao.insert(record)
        }
    }
}