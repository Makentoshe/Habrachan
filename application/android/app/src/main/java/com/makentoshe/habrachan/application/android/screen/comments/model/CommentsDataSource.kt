package com.makentoshe.habrachan.application.android.screen.comments.model

import androidx.paging.PagingSource
import com.makentoshe.habrachan.application.core.arena.Arena
import com.makentoshe.habrachan.entity.Comment
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.request.GetCommentsRequest

class CommentsDataSource(
    private val session: UserSession, private val arena: Arena<GetCommentsRequest, List<Comment>>
) : PagingSource<CommentsDataSource.CommentsSpec, CommentModel>() {
    override suspend fun load(params: LoadParams<CommentsSpec>): LoadResult<CommentsSpec, CommentModel> {
        return arena.suspendFetch(GetCommentsRequest(session, params.key!!.articleId)).fold({ comments ->
            LoadResult.Page(buildModelsFromList(comments).filter { it.parent == null }, null, null, 0, 0)
        }, {
            LoadResult.Error(it)
        })
    }

    /** Spec for requesting comments for article by [articleId] and organizing comments */
    data class CommentsSpec(val articleId: Int)
}

