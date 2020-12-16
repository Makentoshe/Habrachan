package com.makentoshe.habrachan.application.android.arena

import android.util.Log
import com.makentoshe.habrachan.application.android.database.ArticlesDao
import com.makentoshe.habrachan.application.core.arena.ArenaCache
import com.makentoshe.habrachan.application.core.arena.ArenaStorageException
import com.makentoshe.habrachan.network.request.GetArticleRequest
import com.makentoshe.habrachan.network.response.ArticleResponse

// TODO implement
class ArticleArenaCache(
    private val articleDao: ArticlesDao
) : ArenaCache<GetArticleRequest, ArticleResponse> {

    companion object {
        private inline fun capture(level: Int, message: () -> String) {
            Log.println(level, "ArticleArenaCache", message())
        }
    }

    override fun fetch(key: GetArticleRequest): Result<ArticleResponse> {
        capture(Log.DEBUG) { "Fetch article from cache by key: $key" }
        return try {
            val record = articleDao.getById(key.id)
            capture(Log.INFO) { "Article(${record?.id}) fetched from cache" }
            if (record != null) {
                Result.success(ArticleResponse(record.toArticle(), ""))
            } else {
                Result.failure(ArenaStorageException("ArticleArenaCache: record is null"))
            }
        } catch (exception: Exception) {
            capture(Log.INFO){ "Couldn't fetch article: $exception" }
            Result.failure(ArenaStorageException("ArticlesArenaCache").initCause(exception))
        }
    }

    override fun carry(key: GetArticleRequest, value: ArticleResponse) {
        capture(Log.WARN) { "Ignore carry $key" }
    }
}