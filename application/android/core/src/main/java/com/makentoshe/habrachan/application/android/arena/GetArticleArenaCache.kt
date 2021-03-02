package com.makentoshe.habrachan.application.android.arena

import android.util.Log
import com.makentoshe.habrachan.application.android.database.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.database.record.ArticleRecord
import com.makentoshe.habrachan.application.android.database.record.FlowRecord
import com.makentoshe.habrachan.application.android.database.record.HubRecord
import com.makentoshe.habrachan.application.android.database.record.UserRecord
import com.makentoshe.habrachan.application.core.arena.ArenaCache
import com.makentoshe.habrachan.application.core.arena.ArenaStorageException
import com.makentoshe.habrachan.network.request.GetArticleRequest2
import com.makentoshe.habrachan.network.response.GetArticleResponse2
import com.makentoshe.habrachan.network.response.getArticleResponse

class GetArticleArenaCache(
    private val cacheDatabase: AndroidCacheDatabase
) : ArenaCache<GetArticleRequest2, GetArticleResponse2> {

    companion object {
        private inline fun capture(level: Int, message: () -> String) {
            Log.println(level, "GetArticleArenaCache", message())
        }
    }

    override fun fetch(key: GetArticleRequest2): Result<GetArticleResponse2> {
        capture(Log.DEBUG) { "Fetch article from cache by key: $key" }
        return try {
            val record = cacheDatabase.articlesSearchDao().getById(key.articleId.articleId)
            capture(Log.INFO) { "Article(${record?.id}) fetched from cache" }
            if (record != null) {
                val article = record.toArticle(cacheDatabase.badgeDao(), cacheDatabase.hubDao(), cacheDatabase.flowDao(), cacheDatabase.userDao())
                Result.success(getArticleResponse(key, article))
            } else {
                Result.failure(ArenaStorageException("ArticleArenaCache: ArticleRecord is null"))
            }
        } catch (exception: Exception) {
            capture(Log.INFO) { "Couldn't fetch article: $exception" }
            Result.failure(ArenaStorageException("ArticlesArenaCache").initCause(exception))
        }
    }

    override fun carry(key: GetArticleRequest2, value: GetArticleResponse2) {
        capture(Log.INFO) { "Carry search articles to cache with key: $key" }

        cacheDatabase.articlesSearchDao().insert(ArticleRecord(value.article))
        cacheDatabase.userDao().insert(UserRecord(value.article.author))
        value.article.flows.map(::FlowRecord).forEach(cacheDatabase.flowDao()::insert)
        value.article.hubs.forEach { hub ->
            cacheDatabase.hubDao().insert(HubRecord(hub))
//            hub.flow?.let(::FlowRecord)?.let(cacheDatabase.flowDao()::insert)
        }
    }
}