package com.makentoshe.habrachan.application.android.common.comment.viewmodel

import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.entity.CommentId
import com.makentoshe.habrachan.entity.commentId

/**
 * Spec for requesting replies for [commentId] in [articleId].
 * If [commentId] is 0, all replies for the selected article will be returned.
 * */
data class GetArticleCommentsSpec(val articleId: ArticleId, val commentId: CommentId = commentId(0))