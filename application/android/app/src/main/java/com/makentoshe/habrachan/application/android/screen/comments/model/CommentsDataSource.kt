package com.makentoshe.habrachan.application.android.screen.comments.model

import androidx.paging.PagingSource
import com.makentoshe.habrachan.application.core.arena.Arena
import com.makentoshe.habrachan.entity.Comment
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.request.GetCommentsRequest

class CommentsDataSource(
    private val session: UserSession,
    private val arena: Arena<GetCommentsRequest, List<Comment>>,
    private val maxLevelIncluded: Int
) : PagingSource<CommentsDataSource.CommentsSpec, CommentAdapterModel>() {

    override suspend fun load(params: LoadParams<CommentsSpec>): LoadResult<CommentsSpec, CommentAdapterModel> {
        return arena.suspendFetch(GetCommentsRequest(session, params.key!!.articleId)).fold({ comments ->
            LoadResult.Page(CommentAdapterModel.compose(comments, maxLevelIncluded), null, null, 0, 0)
        }, {
            LoadResult.Error(it)
        })
    }

    /** Spec for requesting comments for article by [articleId] */
    data class CommentsSpec(val articleId: Int)
}