package com.makentoshe.habrachan.application.android.common.comment.posting

import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.entity.CommentId
import com.makentoshe.habrachan.entity.commentId

data class PostCommentSpec(
    val articleId: ArticleId,
    val message: String,
    val commentId: CommentId = commentId(0)
)