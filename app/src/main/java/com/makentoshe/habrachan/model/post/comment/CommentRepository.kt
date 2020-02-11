package com.makentoshe.habrachan.model.post.comment

import com.makentoshe.habrachan.common.entity.comment.CommentsResponse
import com.makentoshe.habrachan.common.network.manager.HabrCommentsManager
import com.makentoshe.habrachan.common.network.request.GetCommentsRequest
import io.reactivex.Single

class CommentRepository(
    private val commentsManager: HabrCommentsManager,
    private val commentsRequestFactory: GetCommentsRequest.Factory
) {

    fun get(articleId: Int): Single<CommentsResponse> {
        val request = commentsRequestFactory.build(articleId)
        return commentsManager.getComments(request)
    }
}