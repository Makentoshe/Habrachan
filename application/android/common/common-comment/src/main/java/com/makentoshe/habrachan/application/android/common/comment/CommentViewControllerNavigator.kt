package com.makentoshe.habrachan.application.android.common.comment

interface CommentViewControllerNavigator {

    fun toContentScreen(source: String)
}

interface BlockViewControllerNavigator {

    fun toDiscussionScreen(commentId: Int)
}