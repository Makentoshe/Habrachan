package com.makentoshe.habrachan.application.android.screen.comments.model

/** Spec for requesting replies for [commentId] in [articleId] */
data class CommentsSpec(val articleId: Int, val commentId: Int = 0)