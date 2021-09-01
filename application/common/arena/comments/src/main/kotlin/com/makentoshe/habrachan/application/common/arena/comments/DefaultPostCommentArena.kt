package com.makentoshe.habrachan.application.common.arena.comments

import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.entity.CommentId
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.manager.PostCommentManager
import com.makentoshe.habrachan.network.request.PostCommentRequest

internal class DefaultPostCommentArena(
    private val manager: PostCommentManager<PostCommentRequest>,
) : PostCommentArena(manager) {

    override fun request(userSession: UserSession, articleId: ArticleId, message: String, commentId: CommentId) = manager.request(userSession, articleId, message, commentId)

    override suspend fun suspendCarry(key: PostCommentRequest) = internalSuspendCarry(key)
}