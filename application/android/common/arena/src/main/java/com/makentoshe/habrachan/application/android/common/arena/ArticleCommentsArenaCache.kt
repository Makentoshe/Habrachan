package com.makentoshe.habrachan.application.android.common.arena

import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.database.cache.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.database.cache.record.CommentRecord
import com.makentoshe.habrachan.application.common.arena.ArenaCache
import com.makentoshe.habrachan.application.common.arena.ArenaStorageException
import com.makentoshe.habrachan.entity.Comment
import com.makentoshe.habrachan.functional.Result
import com.makentoshe.habrachan.network.request.GetArticleCommentsRequest
import com.makentoshe.habrachan.network.response.GetArticleCommentsResponse
import javax.inject.Inject

class ArticleCommentsArenaCache @Inject constructor(
    private val database: AndroidCacheDatabase,
) : ArenaCache<GetArticleCommentsRequest, GetArticleCommentsResponse> {

    companion object : Analytics(LogAnalytic()) {
        private const val limit = 3000
        private const val step = 500
    }

    private val commentDao = database.commentDao()

    override fun fetch(key: GetArticleCommentsRequest): Result<GetArticleCommentsResponse> = try {
        val records = database.commentDao().getByArticleId(key.articleId.articleId)
        capture(analyticEvent { "Fetched ${records.size} comments by key: $key" })
        if (records.isEmpty()) {
            Result.failure(ArenaStorageException("CommentsArenaCache"))
        } else {
            Result.success(getArticleCommentsResponse(key, records.map { it.toComment() }))
        }
    } catch (exception: Exception) {
        capture(analyticEvent { "Could not fetch comments: $exception" })
        Result.failure(ArenaStorageException("CommentsArenaCache").initCause(exception))
    }

    override fun carry(key: GetArticleCommentsRequest, value: GetArticleCommentsResponse) {
        if (commentDao.count() > limit) capture(analyticEvent {
            // TODO implement removing oldest elements
            "TODO: Removing oldest $step elements from cache"
        })

        // TODO if the element already placed in the database - reset it lifecycle index
        capture(analyticEvent { "Carry ${value.data.size} comments to cache with key: $key" })
        value.data.map { comment -> CommentRecord(key.articleId.articleId, comment) }.forEach { record ->
            commentDao.insert(record)
        }
    }

    private fun getArticleCommentsResponse(request: GetArticleCommentsRequest, data: List<Comment>) = object : GetArticleCommentsResponse {
        override val request: GetArticleCommentsRequest = request
        override val data: List<Comment> = data
    }
}