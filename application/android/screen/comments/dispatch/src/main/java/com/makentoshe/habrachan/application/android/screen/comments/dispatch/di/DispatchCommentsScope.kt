package com.makentoshe.habrachan.application.android.screen.comments.dispatch.di

import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.entity.CommentId

data class DispatchCommentsScope(val articleId: ArticleId, val commentId: CommentId)