package com.makentoshe.habrachan.application.android.common.articles.arena

import com.makentoshe.habrachan.api.articles.ArticlesFilter
import com.makentoshe.habrachan.api.articles.findFilter
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.database.AndroidCacheDatabase
import com.makentoshe.habrachan.application.android.database.record.*
import com.makentoshe.habrachan.application.common.arena.ArenaCache3
import com.makentoshe.habrachan.application.common.arena.ArenaStorageException
import com.makentoshe.habrachan.application.common.arena.EmptyArenaStorageException
import com.makentoshe.habrachan.entity.article.Article
import com.makentoshe.habrachan.entity.article.articleId
import com.makentoshe.habrachan.entity.article.author
import com.makentoshe.habrachan.entity.article.author.ArticleAuthor
import com.makentoshe.habrachan.entity.article.hub.ArticleHub
import com.makentoshe.habrachan.entity.article.hub.hubId
import com.makentoshe.habrachan.entity.article.hubs
import com.makentoshe.habrachan.functional.Either2
import com.makentoshe.habrachan.network.articles.get.GetArticlesRequest
import com.makentoshe.habrachan.network.articles.get.GetArticlesResponse
import com.makentoshe.habrachan.network.articles.get.entity.articles
import javax.inject.Inject


class ArticlesArenaCache3 @Inject constructor(
    private val cacheDatabase: AndroidCacheDatabase
) : ArenaCache3<GetArticlesRequest, GetArticlesResponse> {

    companion object : Analytics(LogAnalytic()) {
        private const val SIZE = 20
    }

    override fun fetch(key: GetArticlesRequest): Either2<GetArticlesResponse, ArenaStorageException> = try {
        capture(analyticEvent { "Fetch articles from cache by key: $key" })
        val page = key.extractPageNumberFromRequest()
        if (page is Either2.Right) page else fetch(key, (page as Either2.Left).value, key.filters.typeString)
    } catch (exception: Exception) {
        Either2.Right(ArenaStorageException(exception))
    }

    private fun fetch(key: GetArticlesRequest, page: Int, type: String): Either2<GetArticlesResponse, ArenaStorageException> {
        val offset = (page - 1) * SIZE
        val articleRecords = cacheDatabase.articlesDao3().getDescSortedByTimePublished(offset, SIZE, type)
        val articles = articleRecords.map { articleRecord -> fetchArticle(articleRecord) }

        return if (articles.isEmpty()) {
            Either2.Right(ArenaStorageException(EmptyArenaStorageException("ArenaStorage is empty")))
        } else {
            Either2.Left(GetArticlesResponse(key, articles(articles)))
        }
    }

    private fun fetchArticle(articleRecord: ArticleRecord3): Article {
        val articleAuthor = fetchArticleAuthor(fetchArticleAuthorCrossRef(articleRecord))
        val articleHubs = fetchArticleHubCrossRefList(articleRecord).map(::fetchArticleHub)

        return articleRecord.toArticle(articleAuthor, articleHubs)
    }

    private fun fetchArticleAuthorCrossRef(articleRecord: ArticleRecord3): ArticleAuthorCrossRef {
        val articleAuthorCrossRef = cacheDatabase.articleAuthorCrossRefDao().getByArticleId(articleRecord.articleId)
        if (articleAuthorCrossRef == null) throw IllegalStateException("") else return articleAuthorCrossRef
    }

    private fun fetchArticleAuthor(articleAuthorCrossRef: ArticleAuthorCrossRef): ArticleAuthor {
        val articleAuthorRecord = cacheDatabase.articleAuthorDao3().getById(articleAuthorCrossRef.articleAuthorId)
        if (articleAuthorRecord == null) throw IllegalStateException("")

        return articleAuthorRecord.toArticleAuthor()
    }

    private fun fetchArticleHubCrossRefList(articleRecord: ArticleRecord3): List<ArticleHubCrossRef> {
        return cacheDatabase.articleHubCrossRefDao().getByArticleId(articleRecord.articleId)
    }

    private fun fetchArticleHub(articleHubCrossRef: ArticleHubCrossRef): ArticleHub {
        val articleHubRecord = cacheDatabase.hubDao3().getByHubId(articleHubCrossRef.hubId) ?: throw IllegalStateException("")

        return articleHubRecord.toArticleHub()
    }

    override fun carry(key: GetArticlesRequest, value: GetArticlesResponse) {
        carry(key, value, key.filters.typeString)
    }

    private fun carry(key: GetArticlesRequest, value: GetArticlesResponse, type: String) {
        key.extractPageNumberFromRequest().fold({ page -> carry(value, type, page) }) {}
    }

    private fun carry(value: GetArticlesResponse, type: String, page: Int) {
        if (page == 1) clearTablesBeforeCarrying(type)

        capture(analyticEvent { "${value.articles.articles.value.size}" })
        value.articles.articles.value.forEach { article -> carryArticle(article, type) }
    }

    private fun clearTablesBeforeCarrying(type: String) {
        capture(analyticEvent { "Clear hubs related with ArticlesCache(type=$type)" })
        for (articleRecord in cacheDatabase.articlesDao3().getAll(type))  {
            cacheDatabase.articleHubCrossRefDao().clearByArticleId(articleRecord.articleId)
        }

        capture(analyticEvent { "Clear ArticlesCache(type=$type)" })
        cacheDatabase.articlesDao().clear(type)
    }

    private fun carryArticle(article: Article, type: String) = try {
        cacheDatabase.articlesDao3().insert(ArticleRecord3(type, article))
        cacheDatabase.articleAuthorDao3().insert(ArticleAuthorRecord3(article.author.value))
        cacheDatabase.articleAuthorCrossRefDao().insert(ArticleAuthorCrossRef(article.articleId.value.articleId, article.author.value.delegate.authorId.value.authorId))
        article.hubs.value.onEach { hub ->
            cacheDatabase.hubDao3().insert(ArticleHubRecord3(hub))
        }.map { hub ->
            ArticleHubCrossRef(article.articleId.value.articleId, hub.hubId.value.hubId)
        }.forEach { articleHubCrossRef ->
            cacheDatabase.articleHubCrossRefDao().insert(articleHubCrossRef)
        }
    } catch (exception: Exception) {
        capture(analyticEvent(throwable = exception))
    }

    private val Array<out ArticlesFilter>.typeString get() = joinToString(", ")

    private fun GetArticlesRequest.extractPageNumberFromRequest(): Either2<Int, ArenaStorageException> {
        val filter = filters.findFilter("page")
        return if (filter.isEmpty) {
            val exception = ArenaStorageException("Error: could not find a \"page\" filter in ${filters.toList()}.")
            Either2.Right(exception.also { capture(analyticEvent(throwable = it)) })
        } else {
            val page = filter.getOrThrow().value.toIntOrNull()
            if (page == null) {
                val exception = ArenaStorageException("Error: could not select a page number in $filter")
                return Either2.Right(exception.also { capture(analyticEvent(throwable = it)) })
            } else {
                Either2.Left(page)
            }
        }
    }

}