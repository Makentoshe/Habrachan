package com.makentoshe.habrachan.application.android.common.article.arena

import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.database.cache.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.database.cache.record.ArticleAuthorRecord
import com.makentoshe.habrachan.application.android.database.cache.record.ArticleHubCrossRef
import com.makentoshe.habrachan.application.android.database.cache.record.ArticleRecord2
import com.makentoshe.habrachan.application.android.database.cache.record.HubRecord2
import com.makentoshe.habrachan.application.common.arena.ArenaCache
import com.makentoshe.habrachan.application.common.arena.ArenaStorageException
import com.makentoshe.habrachan.functional.Result
import com.makentoshe.habrachan.network.request.GetArticleRequest2
import com.makentoshe.habrachan.network.response.GetArticleResponse2
import com.makentoshe.habrachan.network.response.getArticleResponse
import javax.inject.Inject

class ArticleArenaCache @Inject constructor(
    private val cacheDatabase: AndroidCacheDatabase
) : ArenaCache<GetArticleRequest2, GetArticleResponse2> {

    companion object : Analytics(LogAnalytic())

    override fun fetch(key: GetArticleRequest2): Result<GetArticleResponse2> {
        capture(analyticEvent { "Fetch article from cache by key: $key" })
        return try {
            val record = cacheDatabase.articlesDao().getByIdWithHubsAndFlows(key.articleId.articleId)
            capture(analyticEvent { "Article(${record?.article?.articleId}) fetched from cache" })
            if (record != null) {
                Result.success(getArticleResponse(key, record.toArticle()))
            } else {
                Result.failure(ArenaStorageException("ArticleArenaCache: ArticleRecord is null"))
            }
        } catch (exception: Exception) {
            capture(analyticEvent { "Couldn't fetch article: $exception" })
            Result.failure(ArenaStorageException("ArticlesArenaCache").initCause(exception))
        }
    }

    override fun carry(key: GetArticleRequest2, value: GetArticleResponse2) {
        capture(analyticEvent { "Carry search articles to cache with key: $key" })

        cacheDatabase.articlesDao().insert(ArticleRecord2(ArticleRecord2.UNDEFINED, value.article))
        cacheDatabase.articleAuthorDao().insert(ArticleAuthorRecord(value.article.author))

//        TODO[26.06.2021] - list of flows returns Flow(id=0, name=null, alias=null, url=https://habr.com/ru/flows//, path=/flows//, hubsCount=null) for each element
//        value.article.flows.forEach { flow ->
//            cacheDatabase.flowDao().insert(FlowRecord2(flow))
//            cacheDatabase.articleFlowCrossRefDao().insert(ArticleFlowCrossRef(value.article.articleId, flow.flowId))
//        }
        for (hub in value.article.hubs) {
            cacheDatabase.hubDao().insert(HubRecord2(hub))
            cacheDatabase.articleHubCrossRefDao().insert(ArticleHubCrossRef(value.article.articleId, hub.hubId))
        }
    }
}
