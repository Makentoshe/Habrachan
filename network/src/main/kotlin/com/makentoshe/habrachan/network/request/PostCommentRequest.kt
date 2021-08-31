package com.makentoshe.habrachan.network.request

import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.entity.CommentId
import com.makentoshe.habrachan.functional.Option
import com.makentoshe.habrachan.network.UserSession

interface PostCommentRequest {
    val userSession: UserSession

    /** Article where message should be posted */
    val articleId: ArticleId

    /** Comment message to post. Supports html and markdown formats */
    val text: String

    /** Should be None or 0 for replying to the article and specified comment id otherwise */
    val parentId: Option<CommentId>
}

fun postCommentRequest(
    userSession: UserSession,
    articleId: ArticleId,
    text: String,
    parentId: Option<CommentId> = Option.None
) = object : PostCommentRequest {
    override val parentId: Option<CommentId> = parentId
    override val text: String = text
    override val userSession: UserSession = userSession
    override val articleId: ArticleId = articleId
}