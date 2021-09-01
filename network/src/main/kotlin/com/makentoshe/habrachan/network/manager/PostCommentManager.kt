package com.makentoshe.habrachan.network.manager

import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.entity.CommentId
import com.makentoshe.habrachan.entity.commentId
import com.makentoshe.habrachan.functional.Result
import com.makentoshe.habrachan.network.UserSession
import com.makentoshe.habrachan.network.request.PostCommentRequest
import com.makentoshe.habrachan.network.response.PostCommentResponse

interface PostCommentManager<Request : PostCommentRequest> {

    fun request(
        userSession: UserSession,
        articleId: ArticleId,
        text: String,
        parent: CommentId = commentId(0),
    ): Request

    fun post(request: Request): Result<PostCommentResponse>
}
