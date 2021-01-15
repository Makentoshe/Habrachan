package com.makentoshe.habrachan.application.android.screen.comments.model

import androidx.paging.PagingSource
import com.makentoshe.habrachan.application.android.screen.comments.viewmodel.CommentsSpec
import com.makentoshe.habrachan.application.core.arena.Arena
import com.makentoshe.habrachan.entity.Comment
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.request.GetCommentsRequest

class CommentsDataSource(
    private val session: UserSession,
    private val arena: Arena<GetCommentsRequest, List<Comment>>,
    private val levelRange: Int
) : PagingSource<CommentsSpec, CommentModelElement>() {

    override suspend fun load(params: LoadParams<CommentsSpec>): LoadResult<CommentsSpec, CommentModelElement> {
        return arena.suspendFetch(GetCommentsRequest(session, params.key!!.articleId)).fold({ comments ->
            LoadResult.Page(CommentModelForest.build(comments).collect(levelRange), null, null, 0, 0)
        }, {
            LoadResult.Error(it)
        })
    }
}