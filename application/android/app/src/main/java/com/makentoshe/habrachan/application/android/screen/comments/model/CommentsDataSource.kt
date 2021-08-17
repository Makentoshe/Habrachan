package com.makentoshe.habrachan.application.android.screen.comments.model

import androidx.paging.PagingSource
import com.makentoshe.habrachan.application.android.common.comment.model.forest.CommentModelElement
import com.makentoshe.habrachan.application.android.common.comment.model.forest.CommentModelForest
import com.makentoshe.habrachan.application.core.arena.comments.CommentsSourceFirstArena
import com.makentoshe.habrachan.network.UserSession

class CommentsDataSource(
    private val session: UserSession,
    private val arena: CommentsSourceFirstArena,
    private val levelRange: Int
) : PagingSource<CommentsSpec, CommentModelElement>() {

    override suspend fun load(params: LoadParams<CommentsSpec>): LoadResult<CommentsSpec, CommentModelElement> {
        return arena.suspendFetch(arena.articleCommentsManager.request(session, params.key!!.articleId)).fold({ comments ->
            LoadResult.Page(CommentModelForest.build(comments).collect(levelRange), null, null, 0, 0)
        }, {
            LoadResult.Error(it)
        })
    }
}