package com.makentoshe.habrachan.application.common.arena.comments

import com.makentoshe.habrachan.application.common.arena.CarryArena
import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.entity.CommentId
import com.makentoshe.habrachan.entity.commentId
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.manager.PostCommentManager
import com.makentoshe.habrachan.network.request.PostCommentRequest
import com.makentoshe.habrachan.network.response.PostCommentResponse
import javax.inject.Inject

abstract class PostCommentArena internal constructor(
    private val manager: PostCommentManager<PostCommentRequest>
) : CarryArena<PostCommentRequest, PostCommentResponse>() {

    abstract fun request(userSession: UserSession, articleId: ArticleId, message: String, commentId: CommentId = commentId(0)): PostCommentRequest

    override suspend fun internalSuspendCarry(key: PostCommentRequest) = manager.post(key)

    class Factory @Inject constructor(
        private val manager: PostCommentManager<PostCommentRequest>
    ) {
        fun defaultArena(): PostCommentArena {
            return DefaultPostCommentArena(manager)
        }
    }
}