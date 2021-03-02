package com.makentoshe.habrachan.application.android.arena

import android.util.Log
import com.makentoshe.habrachan.application.android.BuildConfig
import com.makentoshe.habrachan.application.android.database.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.database.record.*
import com.makentoshe.habrachan.application.core.arena.ArenaCache
import com.makentoshe.habrachan.application.core.arena.ArenaStorageException
import com.makentoshe.habrachan.network.request.GetArticlesRequest2
import com.makentoshe.habrachan.network.response.GetArticlesResponse2
import com.makentoshe.habrachan.network.response.getArticlesResponse

class GetArticlesArenaCache(
    private val cacheDatabase: AndroidCacheDatabase
) : ArenaCache<GetArticlesRequest2, GetArticlesResponse2> {

    companion object {
        fun capture(priority: Int, string: String) {
            if (BuildConfig.DEBUG) Log.println(priority, "ArticlesArenaCache", string)
        }
    }

    override fun fetch(key: GetArticlesRequest2): Result<GetArticlesResponse2> {
        capture(Log.INFO, "Fetch search articles from cache by key: $key")
        return try {
            val offset = (key.page - 1) * GetArticlesRequest2.DEFAULT_PAGE_SIZE
            val records = cacheDatabase.articlesDao2()
                .getTimePublishedDescSortedWithHubs(offset, GetArticlesRequest2.DEFAULT_PAGE_SIZE)
            val articles = records.map { it.toArticle() }
            capture(Log.INFO, "Fetched ${articles.size} articles with offset: $offset")
            if (articles.isEmpty()) {
                Result.failure(ArenaStorageException("ArticlesArenaCache"))
            } else {
                Result.success(getArticlesResponse(articles))
            }
        } catch (exception: Exception) {
            capture(Log.INFO, "Could not fetch articles: $exception")
            Result.failure(ArenaStorageException("ArticlesArenaCache").initCause(exception))
        }
    }

    // TODO refactor cleaning articleAuthor table
    // TODO add separate caches for separate specs (All posts, Interesting, Top and Search)
    override fun carry(key: GetArticlesRequest2, value: GetArticlesResponse2) {
        // clear cache on new search (each search starts from page 1)
        if (key.page == 1) {
            capture(Log.INFO, "Clear search articles cache")
            cacheDatabase.articlesDao2().clear()
            cacheDatabase.articleAuthorDao().clear()
        }

        capture(Log.INFO, "Carry search articles to cache with key: $key")
        value.articles.forEachIndexed { index, article ->
            capture(
                Log.INFO,
                "Carry ${(key.page - 1) * GetArticlesRequest2.DEFAULT_PAGE_SIZE + index} article to cache"
            )
            cacheDatabase.articlesDao2().insert(ArticleRecord2(article))
            cacheDatabase.articleAuthorDao().insert(ArticleAuthorRecord(article.author))

            article.flows.map(::FlowRecord).forEach(cacheDatabase.flowDao()::insert)
            article.hubs.forEach { hub ->
                cacheDatabase.hubDao2().insert(HubRecord2(hub))
                cacheDatabase.articlesDao2().insert(ArticleHubCrossRef(article.articleId, hub.hubId))
            }
        }
    }
}