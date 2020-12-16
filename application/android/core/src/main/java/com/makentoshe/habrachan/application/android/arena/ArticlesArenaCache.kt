package com.makentoshe.habrachan.application.android.arena

import android.util.Log
import com.makentoshe.habrachan.application.android.BuildConfig
import com.makentoshe.habrachan.application.android.database.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.database.record.ArticleRecord
import com.makentoshe.habrachan.application.android.database.record.FlowRecord
import com.makentoshe.habrachan.application.android.database.record.HubRecord2
import com.makentoshe.habrachan.application.core.arena.ArenaCache
import com.makentoshe.habrachan.application.core.arena.ArenaStorageException
import com.makentoshe.habrachan.entity.NextPage
import com.makentoshe.habrachan.network.request.GetArticlesRequest
import com.makentoshe.habrachan.network.response.ArticlesResponse

class ArticlesArenaCache(
    private val cacheDatabase: AndroidCacheDatabase
) : ArenaCache<GetArticlesRequest, ArticlesResponse> {

    companion object {
        fun debug(priority: Int, string: String) {
            if (BuildConfig.DEBUG) Log.println(priority, "ArticlesArenaCache", string)
        }
    }

    // TODO add sorting by time_published and other
    // https://medium.com/androiddevelopers/room-time-2b4cf9672b98
    override fun fetch(key: GetArticlesRequest): Result<ArticlesResponse> {
        debug(Log.DEBUG, "Fetch search articles from cache by key: $key")
        return try {
            val start = (key.page - 1) * key.count
            val end = start + key.count - 1
            val articleRecords = cacheDatabase.articlesSearchDao().getInRange(start, end)
            val articles = articleRecords.sortedBy { it.databaseIndex }.map { it.toArticle() }
            debug(Log.INFO, "Fetched ${articles.size} articles from $start to $end")
            if (articles.isEmpty()) {
                Result.failure(ArenaStorageException("ArticlesArenaCache"))
            } else {
                Result.success(ArticlesResponse(articles, NextPage(key.page + 1, ""), -1, "", "", null))
            }
        } catch (exception: Exception) {
            debug(Log.INFO, "Could not fetch articles: $exception")
            Result.failure(ArenaStorageException("ArticlesArenaCache").initCause(exception))
        }
    }

    // TODO add separate caches for separate specs (All posts, Interesting, Top and Search)
    override fun carry(key: GetArticlesRequest, value: ArticlesResponse) {
        // clear cache on new search (each search starts from page 1)
        if (key.page == 1) {
            debug(Log.INFO, "Clear search articles cache")
            cacheDatabase.articlesSearchDao().clear()
        }

        debug(Log.DEBUG, "Carry search articles to cache with key: $key")
        value.data.forEachIndexed { index, article ->
            val databaseIndex = (key.page - 1) * key.count + index
            debug(Log.INFO, "Carry $databaseIndex article to cache")
            cacheDatabase.articlesSearchDao().insert(ArticleRecord(databaseIndex, article))

            article.flows.map(::FlowRecord).forEach(cacheDatabase.flowDao()::insert)
            article.hubs.forEach { hub ->
                cacheDatabase.hubDao().insert(HubRecord2(hub))
                hub.flow?.let(::FlowRecord)?.let(cacheDatabase.flowDao()::insert)
            }
        }
    }
}
