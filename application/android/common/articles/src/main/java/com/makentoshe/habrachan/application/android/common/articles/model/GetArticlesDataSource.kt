package com.makentoshe.habrachan.application.android.common.articles.model

import androidx.paging.PagingSource
import com.makentoshe.habrachan.api.articles.ArticlesFilter
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.common.articles.viewmodel.GetArticlesSpec
import com.makentoshe.habrachan.application.android.common.usersession.AndroidUserSession
import com.makentoshe.habrachan.application.android.common.usersession.AndroidUserSessionProvider
import com.makentoshe.habrachan.application.android.common.usersession.toRequestParameters
import com.makentoshe.habrachan.application.common.arena.ArenaException
import com.makentoshe.habrachan.application.common.arena.articles.ArticlesArena3
import com.makentoshe.habrachan.entity.article.Article
import com.makentoshe.habrachan.network.articles.get.GetArticlesRequest
import com.makentoshe.habrachan.network.articles.get.GetArticlesResponse
import com.makentoshe.habrachan.network.articles.get.entity.articles

class GetArticlesDataSource(
    private val androidUserSessionProvider: AndroidUserSessionProvider,
    private val arena: ArticlesArena3,
    private val paginator: GetArticlesPaginator
) : PagingSource<GetArticlesSpec, Article>() {

    companion object : Analytics(LogAnalytic())

    override suspend fun load(params: LoadParams<GetArticlesSpec>): LoadResult<GetArticlesSpec, Article> {
        val articlesSpec = params.key
        capture(analyticEvent { "articlesSpec=$articlesSpec" })

        return if (articlesSpec != null) load(articlesSpec) else {
            LoadResult.Error(IllegalArgumentException("Could not receive ArticlesSpec"))
        }
    }

    private suspend fun load(articlesSpec: GetArticlesSpec): LoadResult<GetArticlesSpec, Article> {
        val androidUserSession = androidUserSessionProvider.get()
            ?: return LoadResult.Error(Exception("AndroidUserSession wasn't provided"))

        return load(androidUserSession, articlesSpec.articlesFilters.toTypedArray())
    }

    private suspend fun load(androidUserSession: AndroidUserSession, articlesFilters: Array<ArticlesFilter>): LoadResult<GetArticlesSpec, Article> {
        val requestParameters = androidUserSession.toRequestParameters()
        val request = GetArticlesRequest(requestParameters, articlesFilters)
        return arena.suspendFetch(request).fold(::loadNext, ::loadError)
    }

    private fun loadNext(response: GetArticlesResponse): LoadResult<GetArticlesSpec, Article> {
        return LoadResult.Page(response.articles.articles.value, nextKey = paginator.next(response).getOrNull(), prevKey = null)
    }

    private fun loadError(exception: ArenaException): LoadResult<GetArticlesSpec, Article> {
        return LoadResult.Error(exception)
    }

}