package com.makentoshe.habrachan.application.android.arena

import android.util.Log
import com.makentoshe.habrachan.application.android.BuildConfig
import com.makentoshe.habrachan.application.android.database.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.database.dao.ArticlesDao2
import com.makentoshe.habrachan.application.android.database.record.*
import com.makentoshe.habrachan.application.core.arena.ArenaCache
import com.makentoshe.habrachan.application.core.arena.ArenaStorageException
import com.makentoshe.habrachan.entity.Article
import com.makentoshe.habrachan.entity.NextPage
import com.makentoshe.habrachan.network.request.GetArticlesRequest
import com.makentoshe.habrachan.network.response.ArticlesResponse

class ArticlesArenaCache(
    private val cacheDatabase: AndroidCacheDatabase
) : ArenaCache<GetArticlesRequest, ArticlesResponse> {

    companion object {
        fun capture(priority: Int, string: String) {
            if (BuildConfig.DEBUG) Log.println(priority, "ArticlesArenaCache", string)
        }
    }

    override fun fetch(key: GetArticlesRequest): Result<ArticlesResponse> {
        capture(Log.INFO, "Fetch search articles from cache by key: $key")
        return try {
            val offset = (key.page - 1) * key.count
            val articleRecords = cacheDatabase.articlesSearchDao().getTimePublishedDescSorted(offset, key.count)
            val articles = articleRecords.map { record ->
                record.toArticle(cacheDatabase.badgeDao(), cacheDatabase.hubDao(), cacheDatabase.flowDao(), cacheDatabase.userDao())
            }
            capture(Log.INFO, "Fetched ${articles.size} articles with offset: $offset")
            if (articles.isEmpty()) {
                Result.failure(ArenaStorageException("ArticlesArenaCache"))
            } else {
                Result.success(ArticlesResponse(articles, NextPage(key.page + 1, ""), -1, "", "", null))
            }
        } catch (exception: Exception) {
            capture(Log.INFO, "Could not fetch articles: $exception")
            Result.failure(ArenaStorageException("ArticlesArenaCache").initCause(exception))
        }
    }

    // TODO add dao for all articles
    override fun carry(key: GetArticlesRequest, value: ArticlesResponse) {
        when(key.spec) {
            is GetArticlesRequest.Spec.All -> carry(cacheDatabase.newArticlesDao(), key, value) {
                NewArticleRecord(it)
            }
            is GetArticlesRequest.Spec.Interesting -> carry(cacheDatabase.interestingArticlesDao(), key, value) {
                InterestingArticleRecord(it)
            }
            is GetArticlesRequest.Spec.Top -> carry(cacheDatabase.topArticlesDao(), key, value) {
                TopArticleRecord(it)
            }
        }
        println("NEW: ${cacheDatabase.newArticlesDao().getAll().size}")
        println("TOP: ${cacheDatabase.topArticlesDao().getAll().size}")
        println("INT: ${cacheDatabase.interestingArticlesDao().getAll().size}")
    }

    private fun <T: ArticleRecord2> carry(articlesDao: ArticlesDao2<T>, key: GetArticlesRequest, value: ArticlesResponse, factory: (Article) -> T) {
        // clear cache on new search (each search starts from page 1)
        if (key.page == 1) {
            capture(Log.INFO, "Clear ${articlesDao.title} articles cache")
            articlesDao.clear()
        }

        capture(Log.INFO, "Carry search articles to cache with key: $key")
        value.data.forEachIndexed { index, article ->
            capture(Log.INFO, "Carry ${(key.page - 1) * key.count + index} article to cache ${articlesDao.title}")
            articlesDao.insert(factory(article))
            cacheDatabase.userDao().insert(UserRecord(article.author))

            article.flows.map(::FlowRecord).forEach(cacheDatabase.flowDao()::insert)
            article.hubs.forEach { hub ->
                cacheDatabase.hubDao().insert(HubRecord(hub))
                hub.flow?.let(::FlowRecord)?.let(cacheDatabase.flowDao()::insert)
            }
        }
    }
}
