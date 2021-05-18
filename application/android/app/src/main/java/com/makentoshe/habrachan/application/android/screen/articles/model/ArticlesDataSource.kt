package com.makentoshe.habrachan.application.android.screen.articles.model

import androidx.paging.PagingSource
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.core.arena.articles.GetArticlesArena
import com.makentoshe.habrachan.network.UserSession

class ArticlesDataSource(
    private val session: UserSession, private val arena: GetArticlesArena
) : PagingSource<ArticlesSpec, ArticleModelElement>() {

    companion object : Analytics(LogAnalytic())

    override suspend fun load(params: LoadParams<ArticlesSpec>): LoadResult<ArticlesSpec, ArticleModelElement> {
        val articlesSpec = params.key
        capture(analyticEvent(javaClass.simpleName, "articlesSpec=${articlesSpec.toString()}"))
        if (articlesSpec == null) {
            return LoadResult.Error(IllegalArgumentException("Could not receive ArticlesSpec"))
        }

        val request = arena.manager.request(session, articlesSpec.page, articlesSpec.specType)
        capture(analyticEvent(javaClass.simpleName, "request=$request"))
        if (request == null) {
            return LoadResult.Error(IllegalArgumentException("GetArticlesRequest is null in ArticlesDataSource"))
        }

        return arena.suspendFetch(request).fold({
            val nextKey = it.pagination.next?.number?.let { articlesSpec.copy(page = it) }
            LoadResult.Page(it.articles.map(::ArticleModelElement), null, nextKey)
        }, { LoadResult.Error(it) })
    }
}