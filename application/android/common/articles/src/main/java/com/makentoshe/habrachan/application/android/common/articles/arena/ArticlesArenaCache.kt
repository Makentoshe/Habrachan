package com.makentoshe.habrachan.application.android.common.articles.arena

import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.database.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.database.record.ArticleAuthorRecord
import com.makentoshe.habrachan.application.android.database.record.ArticleHubCrossRef
import com.makentoshe.habrachan.application.android.database.record.ArticleRecord2
import com.makentoshe.habrachan.application.android.database.record.HubRecord2
import com.makentoshe.habrachan.application.common.arena.ArenaCache
import com.makentoshe.habrachan.application.common.arena.ArenaStorageException
import com.makentoshe.habrachan.functional.Result
import com.makentoshe.habrachan.network.request.GetArticlesRequest2
import com.makentoshe.habrachan.network.request.SpecType
import com.makentoshe.habrachan.network.response.GetArticlesResponse2
import com.makentoshe.habrachan.network.response.Pagination
import com.makentoshe.habrachan.network.response.getArticlesResponse
import javax.inject.Inject

class ArticlesArenaCache @Inject constructor(
    private val cacheDatabase: AndroidCacheDatabase
) : ArenaCache<GetArticlesRequest2, GetArticlesResponse2> {

    companion object : Analytics(LogAnalytic())

    override fun fetch(key: GetArticlesRequest2): Result<GetArticlesResponse2> {
        capture(analyticEvent { "Fetch search articles from cache by key: $key" })
        return when (key.spec.type) {
            is SpecType.All -> fetch(key, ArticleRecord2.Types.ALL)
            is SpecType.Interesting -> fetch(key, ArticleRecord2.Types.INTERESTING)
            is SpecType.Top -> fetch(key, ArticleRecord2.Types.TOP)
            else -> Result.failure(ArenaStorageException("Could not resolve type"))
        }
    }

    /** [type] is a cache type. Can be ALL, INTERESTING, TOP, SUBSCRIBE */
    private fun fetch(key: GetArticlesRequest2, type: String): Result<GetArticlesResponse2> = try {
        val offset = (key.page - 1) * GetArticlesRequest2.DEFAULT_PAGE_SIZE
        val articleRecords = cacheDatabase.articlesDao().getTimePublishedDescSorted(offset, GetArticlesRequest2.DEFAULT_PAGE_SIZE, type)
        val articles = articleRecords.map { it.toArticle() }

        capture(analyticEvent { "Fetched ${articles.size} articles with offset: $offset" })

        if (articles.isEmpty()) {
            Result.failure(ArenaStorageException("ArticlesArenaCache"))
        } else {
            // TODO(medium) add check on next page exist
            val pagination = Pagination(Pagination.Next(key.page + 1, null))
            Result.success(getArticlesResponse(key, articles, pagination))
        }
    } catch (exception: Exception) {
        capture(analyticEvent { "Could not fetch articles: $exception" })
        Result.failure(ArenaStorageException("ArticlesArenaCache").initCause(exception))
    }

    override fun carry(key: GetArticlesRequest2, value: GetArticlesResponse2) {
        when (key.spec.type) {
            is SpecType.All -> carry(key, value, ArticleRecord2.ALL)
            is SpecType.Interesting -> carry(key, value, ArticleRecord2.INTERESTING)
            is SpecType.Top -> carry(key, value, ArticleRecord2.TOP)
        }
    }

    // TODO(medium) clear articleAuthor table
    /** [type] is a cache type. Can be ALL, INTERESTING, TOP, SUBSCRIBE */
    private fun carry(key: GetArticlesRequest2, value: GetArticlesResponse2, type: String) {
        if (key.page == 1) {
            capture(analyticEvent { "Clear flows and hubs cross references, related with $type articles" })
            cacheDatabase.articlesDao().getAll(type).forEach { record ->
                cacheDatabase.articleHubCrossRefDao().clearByArticleId(record.articleId)
                cacheDatabase.articleFlowCrossRefDao().clearByArticleId(record.articleId)
            }
            capture(analyticEvent { "Clear $type articles cache" })
            cacheDatabase.articlesDao().clear(type)
        }

        capture(analyticEvent { "Carry articles to $type cache with key: $key" })

        value.articles.forEach { article ->
            cacheDatabase.articlesDao().insert(ArticleRecord2(type, article))
            cacheDatabase.articleAuthorDao().insert(ArticleAuthorRecord(article.author))

//            TODO[26.06.2021] - list of flows returns Flow(id=0, name=null, alias=null, url=https://habr.com/ru/flows//, path=/flows//, hubsCount=null) for each element
//            article.flows.forEach { flow ->
//                cacheDatabase.flowDao().insert(FlowRecord2(flow))
//                cacheDatabase.articleFlowCrossRefDao().insert(ArticleFlowCrossRef(article.articleId, flow.flowId))
//            }

            article.hubs.forEach { hub ->
                cacheDatabase.hubDao().insert(HubRecord2(hub))
                cacheDatabase.articleHubCrossRefDao().insert(ArticleHubCrossRef(article.articleId, hub.hubId))
            }
        }
    }
}
