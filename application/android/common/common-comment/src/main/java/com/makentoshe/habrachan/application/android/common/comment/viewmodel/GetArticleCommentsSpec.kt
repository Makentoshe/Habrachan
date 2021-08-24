package com.makentoshe.habrachan.application.android.common.comment.viewmodel

import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.entity.CommentId
import com.makentoshe.habrachan.functional.Option

sealed class GetArticleCommentsSpec2 {
    abstract val articleId: ArticleId
    abstract val optionableCommentId: Option<CommentId>

    class ArticleCommentsSpec(
        override val articleId: ArticleId,
    ) : GetArticleCommentsSpec2() {
        override val optionableCommentId: Option<CommentId> = Option.None
    }

    class DiscussionCommentsSpec(
        override val articleId: ArticleId,
        commentId: CommentId,
    ) : GetArticleCommentsSpec2() {
        override val optionableCommentId = Option.Value(commentId)
    }
}