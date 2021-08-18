package com.makentoshe.habrachan.application.android.common.comment.model

import androidx.paging.PagingSource
import com.makentoshe.habrachan.application.android.common.comment.model.forest.CommentModelElement
import com.makentoshe.habrachan.application.android.common.comment.model.forest.CommentModelForest
import com.makentoshe.habrachan.application.android.common.comment.viewmodel.GetArticleCommentsSpec
import com.makentoshe.habrachan.application.common.arena.comments.ArticleCommentsArena
import com.makentoshe.habrachan.functional.fold
import com.makentoshe.habrachan.functional.map
import com.makentoshe.habrachan.network.UserSession

class ArticleCommentsDataSource(
    private val session: UserSession,
    private val arena: ArticleCommentsArena,
    private val levelRange: Int,
) : PagingSource<GetArticleCommentsSpec, CommentModelElement>() {

    override suspend fun load(
        params: LoadParams<GetArticleCommentsSpec>,
    ): LoadResult<GetArticleCommentsSpec, CommentModelElement> {
        val articleId = params.key?.articleId ?: throw IllegalArgumentException("ArticleId should be provided.")
        val request = arena.request(session, articleId.articleId)
        return arena.suspendFetch(request).map { response ->
            CommentModelForest.build(response.data).collect(levelRange)
        }.fold({ flattenForest ->
            LoadResult.Page(flattenForest, prevKey = null, nextKey = null, itemsAfter = 0)
        }, {
            LoadResult.Error(it)
        })
    }
}