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
            LoadResult.Page(composeModels(comments, params.key!!), null, null, 0, 0)
        }, {
            LoadResult.Error(it)
        })
    }

    private fun composeModels(comments: List<Comment>, spec: CommentsSpec): List<CommentModel> {
        val models = buildModelsFromList(comments)
        return if (spec.commentId <= 0) { // if proper commentId does not defined - return root comments
            models.filter { it.parent == null }
        } else { // return parent comment model
            models.filter { it.comment.id == spec.commentId }
        }
    }

    /** Spec for requesting comments for article by [articleId] and organizing comments by [commentId]*/
    data class CommentsSpec(val articleId: Int, val commentId: Int = 0)
}

