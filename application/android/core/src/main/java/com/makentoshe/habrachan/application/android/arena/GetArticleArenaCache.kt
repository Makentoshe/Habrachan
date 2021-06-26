package com.makentoshe.habrachan.application.android.arena

import android.util.Log
import com.makentoshe.habrachan.application.android.database.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.database.record.ArticleAuthorRecord
import com.makentoshe.habrachan.application.android.database.record.ArticleHubCrossRef
import com.makentoshe.habrachan.application.android.database.record.ArticleRecord2
import com.makentoshe.habrachan.application.android.database.record.HubRecord2
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
            val record = cacheDatabase.articlesDao().getByIdWithHubsAndFlows(key.articleId.articleId)
            capture(Log.INFO) { "Article(${record?.article?.articleId}) fetched from cache" }
            if (record != null) {
                Result.success(getArticleResponse(key, record.toArticle()))
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

        cacheDatabase.articlesDao().insert(ArticleRecord2(ArticleRecord2.UNDEFINED, value.article))
        cacheDatabase.articleAuthorDao().insert(ArticleAuthorRecord(value.article.author))

//        TODO[26.06.2021] - list of flows returns Flow(id=0, name=null, alias=null, url=https://habr.com/ru/flows//, path=/flows//, hubsCount=null) for each element
//        value.article.flows.forEach { flow ->
//            cacheDatabase.flowDao().insert(FlowRecord2(flow))
//            cacheDatabase.articleFlowCrossRefDao().insert(ArticleFlowCrossRef(value.article.articleId, flow.flowId))
//        }
        value.article.hubs.forEach { hub ->
            cacheDatabase.hubDao().insert(HubRecord2(hub))
            cacheDatabase.articleHubCrossRefDao().insert(ArticleHubCrossRef(value.article.articleId, hub.hubId))
        }
    }
}