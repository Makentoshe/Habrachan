package com.makentoshe.habrachan.application.android.screen.articles.model

import androidx.paging.PagingSource
import com.makentoshe.habrachan.application.core.arena.articles.GetArticlesArena
import com.makentoshe.habrachan.network.UserSession

class ArticlesDataSource(
    private val session: UserSession, private val arena: GetArticlesArena
) : PagingSource<ArticlesSpec, ArticleModelElement>() {

    override suspend fun load(params: LoadParams<ArticlesSpec>): LoadResult<ArticlesSpec, ArticleModelElement> {
        val articlesSpec = params.key
            ?: return LoadResult.Error(IllegalArgumentException("Could not receive ArticlesSpec"))

        val request = arena.manager.request(session, articlesSpec.page, articlesSpec.specType)
            ?: return LoadResult.Error(IllegalArgumentException("GetArticlesRequest is null in ArticlesDataSource"))

        return arena.suspendFetch(request).fold({
            val nextKey = params.key?.copy(page = params.key!!.page + 1)
            val prevKey = if (params.key?.page == 1) null else params.key!!.copy(page = params.key!!.page - 1)
            LoadResult.Page(it.articles.map(::ArticleModelElement), prevKey, nextKey)
        }, { LoadResult.Error(it) })
    }
}