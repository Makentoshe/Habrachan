package com.makentoshe.habrachan.application.android.arena

import android.util.Log
import com.makentoshe.habrachan.application.android.database.AndroidCacheDatabase
import com.makentoshe.habrachan.application.core.arena.ArenaCache
import com.makentoshe.habrachan.network.request.GetArticleRequest2
import com.makentoshe.habrachan.network.response.GetArticleResponse2

class ArticleArenaCache(
    private val cacheDatabase: AndroidCacheDatabase
) : ArenaCache<GetArticleRequest2, GetArticleResponse2> {

    companion object {
        private inline fun capture(level: Int, message: () -> String) {
            Log.println(level, "ArticleArenaCache", message())
        }
    }

    override fun fetch(key: GetArticleRequest2): Result<GetArticleResponse2> {
        capture(Log.DEBUG) { "Fetch article from cache by key: $key" }
        return Result.failure(Exception("TODO"))
//        return try {
//            val record = fetchRecordById(key.articleId.articleId)
//            capture(Log.INFO) { "Article(${record?.id}) fetched from cache" }
//            if (record != null) {
//                val article = record.toArticle(
//                    cacheDatabase.badgeDao(), cacheDatabase.hubDao(), cacheDatabase.flowDao(), cacheDatabase.userDao()
//                )
//                Result.success(getArticleResponse(key, article))
//            } else {
//                Result.failure(ArenaStorageException("ArticleArenaCache: Could not find ArticleRecord in caches"))
//            }
//        } catch (exception: Exception) {
//            capture(Log.INFO) { "Couldn't fetch article: $exception" }
//            Result.failure(ArenaStorageException("ArticlesArenaCache").initCause(exception))
//        }
    }
//
//    private fun fetchRecordById(id: Int): ArticleRecord? {
//        return cacheDatabase.tempArticlesDao().getById(id) ?: cacheDatabase.newArticlesDao().getById(id)
//        ?: cacheDatabase.interestingArticlesDao().getById(id) ?: cacheDatabase.topArticlesDao().getById(id)
//    }

    // TODO(high) finish
    override fun carry(key: GetArticleRequest2, value: GetArticleResponse2) {
        capture(Log.INFO) { "Carry search articles to cache with key: $key" }

//        cacheDatabase.tempArticlesDao().insert(TempArticleRecord(value.article))
//        cacheDatabase.userDao().insert(UserRecord(value.article.author))
//        value.article.flows.map(::FlowRecord).forEach(cacheDatabase.flowDao()::insert)
//        value.article.hubs.forEach { hub ->
//            cacheDatabase.hubDao().insert(HubRecord(hub))
//            hub.flow?.let(::FlowRecord)?.let(cacheDatabase.flowDao()::insert)
//        }
    }
}