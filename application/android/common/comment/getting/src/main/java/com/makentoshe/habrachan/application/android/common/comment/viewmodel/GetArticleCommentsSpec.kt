package com.makentoshe.habrachan.application.android.common.comment.viewmodel

import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.entity.CommentId
import com.makentoshe.habrachan.functional.Option

sealed class GetArticleCommentsSpec2 {
    abstract val articleId: ArticleId
    abstract val commentIdOption: Option<CommentId>

    override fun toString(): String {
        return "${this::class.simpleName}(articleId=${articleId.articleId}, commentId=${commentIdOption.getOrNull()?.commentId})"
    }

    class ArticleCommentsSpec(
        override val articleId: ArticleId,
    ) : GetArticleCommentsSpec2() {
        override val commentIdOption: Option<CommentId> = Option.None
    }

    class ThreadCommentsSpec(
        override val articleId: ArticleId,
        commentId: CommentId,
    ) : GetArticleCommentsSpec2() {
        override val commentIdOption = Option.Value(commentId)
    }

    class DispatchCommentsSpec(
        override val articleId: ArticleId,
        commentId: CommentId,
    ) : GetArticleCommentsSpec2() {
        override val commentIdOption = Option.Value(commentId)
    }
}