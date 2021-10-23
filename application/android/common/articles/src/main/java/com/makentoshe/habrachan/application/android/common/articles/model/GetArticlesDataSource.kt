package com.makentoshe.habrachan.application.android.common.articles.model

import androidx.paging.PagingSource
import com.makentoshe.habrachan.api.AdditionalRequestParameters
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.common.articles.viewmodel.GetArticlesSpec
import com.makentoshe.habrachan.application.common.arena.ArenaException
import com.makentoshe.habrachan.application.common.arena.articles.ArticlesArena3
import com.makentoshe.habrachan.entity.article.Article
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.articles.get.GetArticlesRequest
import com.makentoshe.habrachan.network.articles.get.GetArticlesResponse
import com.makentoshe.habrachan.network.articles.get.entity.articles

class GetArticlesDataSource(
    private val session: UserSession,
    private val arena: ArticlesArena3,
    private val paginator: GetArticlesPaginator
) : PagingSource<GetArticlesSpec, Article>() {

    companion object : Analytics(LogAnalytic())

    private fun UserSession.toRequestParameters(): AdditionalRequestParameters {
        val queries = mapOf("fl" to "en%2Cru")
        val headers = mapOf("apiKey" to api, "client" to client, "token" to token)
        return AdditionalRequestParameters(headers, queries)
    }

    override suspend fun load(params: LoadParams<GetArticlesSpec>): LoadResult<GetArticlesSpec, Article> {
        val articlesSpec = params.key
        capture(analyticEvent { "articlesSpec=$articlesSpec" })

        return if (articlesSpec != null) load(articlesSpec) else {
            LoadResult.Error(IllegalArgumentException("Could not receive ArticlesSpec"))
        }
    }

    private suspend fun load(articlesSpec: GetArticlesSpec): LoadResult<GetArticlesSpec, Article> {
        val request = GetArticlesRequest(session.toRequestParameters(), articlesSpec.articlesFilters.toTypedArray())
        return arena.suspendFetch(request).fold(::loadNext, ::loadError)
    }

    private fun loadNext(response: GetArticlesResponse): LoadResult<GetArticlesSpec, Article> {
        return LoadResult.Page(response.articles.articles.value, nextKey = paginator.next(response).getOrNull(), prevKey = null)
    }

    private fun loadError(exception: ArenaException): LoadResult<GetArticlesSpec, Article> {
        return LoadResult.Error(exception)
    }

}