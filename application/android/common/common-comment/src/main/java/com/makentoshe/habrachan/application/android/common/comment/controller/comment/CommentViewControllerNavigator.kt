package com.makentoshe.habrachan.application.android.common.comment.controller.comment

interface CommentViewControllerNavigator {

    fun toContentScreen(source: String)

    fun toDetailsScreen(commentId: Int)

    fun toUserScreen(login: String)
}

