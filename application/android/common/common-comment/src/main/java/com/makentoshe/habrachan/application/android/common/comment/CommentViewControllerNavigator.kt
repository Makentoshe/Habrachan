package com.makentoshe.habrachan.application.android.common.comment

interface CommentViewControllerNavigator {

    fun toContentScreen(source: String)

    fun toDetailsScreen(commentId: Int)
}

interface BlockViewControllerNavigator {

    fun toDiscussionScreen(commentId: Int)
}