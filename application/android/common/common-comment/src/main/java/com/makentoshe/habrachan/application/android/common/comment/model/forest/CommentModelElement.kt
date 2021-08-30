package com.makentoshe.habrachan.application.android.common.comment.model.forest

import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.entity.Comment

interface CommentModelElement {

    val comment: Comment

    val level: Int

    val articleId: ArticleId
}

fun CommentModelElement.copy(
    comment: Comment = this.comment,
    level: Int = this.level,
    articleId: ArticleId = this.articleId
) = object : CommentModelElement {
    override val comment: Comment = comment
    override val level: Int = level
    override val articleId: ArticleId = articleId
}