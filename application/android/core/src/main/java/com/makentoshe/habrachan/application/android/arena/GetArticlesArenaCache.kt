package com.makentoshe.habrachan.application.android.arena

import android.util.Log
import com.makentoshe.habrachan.application.android.BuildConfig
import com.makentoshe.habrachan.application.android.database.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.database.dao.article.ArticlesDao
import com.makentoshe.habrachan.application.android.database.record.*
import com.makentoshe.habrachan.application.android.database.record.article.ArticleRecord
import com.makentoshe.habrachan.application.android.database.record.article.InterestingArticleRecord
import com.makentoshe.habrachan.application.android.database.record.article.NewArticleRecord
import com.makentoshe.habrachan.application.android.database.record.article.TopArticleRecord
import com.makentoshe.habrachan.application.core.arena.ArenaCache
import com.makentoshe.habrachan.application.core.arena.ArenaStorageException
import com.makentoshe.habrachan.entity.Article
import com.makentoshe.habrachan.network.request.GetArticlesRequest2
import com.makentoshe.habrachan.network.response.GetArticlesResponse2

class ArticlesArenaCache(
    private val cacheDatabase: AndroidCacheDatabase
) : ArenaCache<GetArticlesRequest2, GetArticlesResponse2> {

    companion object {
        fun capture(priority: Int, string: String) {
            if (BuildConfig.DEBUG) Log.println(priority, "ArticlesArenaCache", string)
        }
    }

    override fun fetch(key: GetArticlesRequest2): Result<GetArticlesResponse2> {
        capture(Log.INFO, "Fetch search articles from cache by key: $key")
        return Result.failure(Exception("TODO"))
//        return when (key.spec) {
//            is GetArticlesRequest2.Spec.All -> fetch(cacheDatabase.newArticlesDao(), key)
//            is GetArticlesRequest2.Spec.Interesting -> fetch(cacheDatabase.interestingArticlesDao(), key)
//            is GetArticlesRequest2.Spec.Top -> fetch(cacheDatabase.topArticlesDao(), key)
//        }
    }

//    private fun <T : ArticleRecord> fetch(
//        articlesDao: ArticlesDao<T>,
//        key: GetArticlesRequest2
//    ): Result<GetArticlesResponse2> {
//        return try {
//            val offset = (key.page - 1) * key.count
//            val articleRecords = articlesDao.getTimePublishedDescSorted(offset, key.count)
//            val articles = articleRecords.map { record ->
//                record.toArticle(
//                    cacheDatabase.badgeDao(),
//                    cacheDatabase.hubDao(),
//                    cacheDatabase.flowDao(),
//                    cacheDatabase.userDao()
//                )
//            }
//            capture(Log.INFO, "Fetched ${articles.size} articles with offset: $offset")
//            if (articles.isEmpty()) {
//                Result.failure(ArenaStorageException("ArticlesArenaCache"))
//            } else {
//                Result.success(GetArticlesResponse2(articles, NextPage(key.page + 1, ""), -1, "", "", null))
//            }
//        } catch (exception: Exception) {
//            capture(Log.INFO, "Could not fetch articles: $exception")
//            Result.failure(ArenaStorageException("ArticlesArenaCache").initCause(exception))
//        }
//    }

    override fun carry(key: GetArticlesRequest2, value: GetArticlesResponse2) {
//        when(key.spec) {
//            is GetArticlesRequest2.Spec.All -> carry(cacheDatabase.newArticlesDao(), key, value) {
//                NewArticleRecord(it)
//            }
//            is GetArticlesRequest2.Spec.Interesting -> carry(cacheDatabase.interestingArticlesDao(), key, value) {
//                InterestingArticleRecord(it)
//            }
//            is GetArticlesRequest2.Spec.Top -> carry(cacheDatabase.topArticlesDao(), key, value) {
//                TopArticleRecord(it)
//            }
//        }
    }

//    private fun <T : ArticleRecord> carry(
//        articlesDao: ArticlesDao<T>,
//        key: GetArticlesRequest2,
//        value: GetArticlesResponse2,
//        factory: (Article) -> T
//    ) {
//        // clear cache on new search (each search starts from page 1)
//        if (key.page == 1) {
//            capture(Log.INFO, "Clear ${articlesDao.title} articles cache")
//            articlesDao.clear()
//        }
//
//        capture(Log.INFO, "Carry search articles to cache with key: $key")
//        value.data.forEachIndexed { index, article ->
//            capture(Log.INFO, "Carry ${(key.page - 1) * key.count + index} article to cache ${articlesDao.title}")
//            articlesDao.insert(factory(article))
//            cacheDatabase.userDao().insert(UserRecord(article.author))
//
//            article.flows.map(::FlowRecord).forEach(cacheDatabase.flowDao()::insert)
//            article.hubs.forEach { hub ->
//                cacheDatabase.hubDao().insert(HubRecord(hub))
//                hub.flow?.let(::FlowRecord)?.let(cacheDatabase.flowDao()::insert)
//            }
//        }
//    }
}
