package com.makentoshe.habrachan.application.android.common.comment.model.forest

import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.entity.Comment

interface CommentModelElement {

    val comment: Comment

    val level: Int

    val articleId: ArticleId
}