package com.makentoshe.habrachan.application.android.common.articles.model

import androidx.paging.PagingSource
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.common.articles.viewmodel.GetArticlesSpec
import com.makentoshe.habrachan.application.common.arena.articles.ArticlesArena
import com.makentoshe.habrachan.functional.fold
import com.makentoshe.habrachan.network.UserSession

class GetArticlesDataSource(
    private val session: UserSession, private val arena: ArticlesArena
) : PagingSource<GetArticlesSpec, ArticlesModelElement>() {

    companion object : Analytics(LogAnalytic())

    override suspend fun load(params: LoadParams<GetArticlesSpec>): LoadResult<GetArticlesSpec, ArticlesModelElement> {
        val articlesSpec = params.key
        capture(analyticEvent { "articlesSpec=$articlesSpec" })
        if (articlesSpec == null) {
            return LoadResult.Error(IllegalArgumentException("Could not receive ArticlesSpec"))
        }

        val request = arena.request(session, articlesSpec.page, articlesSpec.specType)
        capture(analyticEvent { "request=$request" })
        if (request == null) {
            return LoadResult.Error(IllegalArgumentException("GetArticlesRequest is null in ArticlesDataSource"))
        }

        return arena.suspendFetch(request).fold({
            val nextKey = it.pagination.next?.number?.let { articlesSpec.copy(page = it) }
            LoadResult.Page(it.articles.map(::ArticlesModelElement), null, nextKey)
        }, { LoadResult.Error(it) })
    }
}