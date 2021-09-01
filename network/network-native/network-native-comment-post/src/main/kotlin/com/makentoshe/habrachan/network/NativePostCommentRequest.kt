package com.makentoshe.habrachan.network

import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.entity.CommentId
import com.makentoshe.habrachan.entity.commentId
import com.makentoshe.habrachan.network.request.PostCommentRequest

data class NativePostCommentRequest(
    override val userSession: UserSession,
    override val articleId: ArticleId,
    override val text: String,
    override val parentId: CommentId = commentId(0)
) : PostCommentRequest


