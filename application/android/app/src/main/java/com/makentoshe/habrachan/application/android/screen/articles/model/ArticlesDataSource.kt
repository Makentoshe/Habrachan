package com.makentoshe.habrachan.application.android.screen.articles.model

import androidx.paging.PagingSource
import com.makentoshe.habrachan.application.android.screen.articles.viewmodel.ArticlesSpec
import com.makentoshe.habrachan.application.core.arena.Arena
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.request.GetArticlesRequest
import com.makentoshe.habrachan.network.response.ArticlesResponse

class ArticlesDataSource(
    private val session: UserSession, private val arena: Arena<GetArticlesRequest, ArticlesResponse>
) : PagingSource<ArticlesSpec, ArticleModelElement>() {
    override suspend fun load(params: LoadParams<ArticlesSpec>): LoadResult<ArticlesSpec, ArticleModelElement> {
        return arena.suspendFetch(GetArticlesRequest(session, params.key!!.page, params.key!!.spec)).fold({
            val nextKey = if (it.nextPage.isSpecified) params.key?.copy(page = it.nextPage.int) else null
            val prevKey = if (params.key?.page == 1) null else params.key!!.copy(page = params.key!!.page - 1)
            LoadResult.Page(it.data.map { ArticleModelElement(it) }, prevKey, nextKey)
        }, { LoadResult.Error(it) })
    }
}