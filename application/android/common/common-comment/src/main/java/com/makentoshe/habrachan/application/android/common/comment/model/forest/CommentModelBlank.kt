package com.makentoshe.habrachan.application.android.common.comment.model.forest

import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.entity.Comment

/** Contains parent comment and reference to a parent node */
data class CommentModelBlank(
    override val comment: Comment, val count: Int, override val level: Int, override val articleId: ArticleId
) : CommentModelElement {
    val parent: CommentModelNode? = null
}