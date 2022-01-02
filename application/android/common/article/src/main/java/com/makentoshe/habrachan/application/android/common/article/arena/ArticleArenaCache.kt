package com.makentoshe.habrachan.application.android.common.article.arena

import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.database.cache.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.database.cache.record.ArticleAuthorRecord3
import com.makentoshe.habrachan.application.android.database.cache.record.ArticleHubCrossRef
import com.makentoshe.habrachan.application.android.database.cache.record.ArticleRecord3
import com.makentoshe.habrachan.application.android.database.cache.record.HubRecord2
import com.makentoshe.habrachan.application.common.arena.ArenaCache3
import com.makentoshe.habrachan.application.common.arena.ArenaStorageException
import com.makentoshe.habrachan.application.common.arena.EmptyArenaStorageException
import com.makentoshe.habrachan.application.common.arena.article.get.*
import com.makentoshe.habrachan.functional.Either2
import javax.inject.Inject

class ArticleArenaCache @Inject constructor(
    private val cacheDatabase: AndroidCacheDatabase
) : ArenaCache3<GetArticleArenaRequest, GetArticleArenaResponse> {

//    override fun fetch(key: GetArticleRequest2): Result<GetArticleResponse2> {
//        capture(analyticEvent { "Fetch article from cache by key: $key" })
//        return try {
//            val record = cacheDatabase.articlesDao().getByIdWithHubsAndFlows(key.articleId.articleId)
//            capture(analyticEvent { "Article(${record?.article?.articleId}) fetched from cache" })
//            if (record != null) {
//                Result.success(getArticleResponse(key, record.toArticle()))
//            } else {
//                Result.failure(ArenaStorageException("ArticleArenaCache: ArticleRecord is null"))
//            }
//        } catch (exception: Exception) {
//            capture(analyticEvent { "Couldn't fetch article: $exception" })
//            Result.failure(ArenaStorageException("ArticlesArenaCache").initCause(exception))
//        }
//    }

    override fun fetch(key: GetArticleArenaRequest): Either2<GetArticleArenaResponse, ArenaStorageException> {
        return Either2.Right(EmptyArenaStorageException())
    }

    // TODO handle collisions between undefined and defined articles
    // TODO handle article clearing
    override fun carry(key: GetArticleArenaRequest, value: GetArticleArenaResponse) = try {
        capture(analyticEvent { "Carry search articles to cache with key: $key" })

        cacheDatabase.articlesDao3().insert(value.article.toArticleRecord(UNDEFINED_ARTICLE_TYPE))
        cacheDatabase.articleAuthorDao3().insert(value.article.author.value.toArticleAuthorRecord())

//        TODO[26.06.2021] - list of flows returns Flow(id=0, name=null, alias=null, url=https://habr.com/ru/flows//, path=/flows//, hubsCount=null) for each element
//        value.article.flows.forEach { flow ->
//            cacheDatabase.flowDao().insert(FlowRecord2(flow))
//            cacheDatabase.articleFlowCrossRefDao().insert(ArticleFlowCrossRef(value.article.articleId, flow.flowId))
//        }
        for (hub in value.article.hubs.value) {
            cacheDatabase.hubDao().insert(hub.toHubRecord())
            cacheDatabase.articleHubCrossRefDao().insert(articleHubCrossRef(value.article, hub))
        }
    } catch (exception: Exception) {
        capture(analyticEvent(throwable = exception))
    }

    private fun ArticleFromArena.toArticleRecord(type: String) = ArticleRecord3(
        articleId = articleId.value.articleId,
        articleTitle = title.value.articleTitle,
        articleText = articleText.getOrNull()?.html,
        scoresCount = scoresCount.value,
        votesCount = votesCount.value,
        favoritesCount = favoritesCount.value,
        commentsCount = commentsCount.value,
        readingCount = readingCount.value,
        timePublished = timePublished.value.timePublishedString,
        internalType = type,
    )

    private fun ArticleAuthorFromArena.toArticleAuthorRecord() = ArticleAuthorRecord3(
        authorId = authorId.value.authorId,
        authorLogin = authorLogin.value.authorLogin,
        authorAvatar = authorAvatar.getOrNull()?.avatarUrl,
    )

    private fun ArticleHubFromArena.toHubRecord() = HubRecord2(
        hubId = hubId.value.hubId,
        title = title.value,
        alias = alias.value,
    )

    private fun articleHubCrossRef(article: ArticleFromArena, hub: ArticleHubFromArena): ArticleHubCrossRef {
        return ArticleHubCrossRef(article.articleId.value.articleId, hub.hubId.value.hubId)
    }

    companion object : Analytics(LogAnalytic()) {
        private const val UNDEFINED_ARTICLE_TYPE = ""
    }
}
