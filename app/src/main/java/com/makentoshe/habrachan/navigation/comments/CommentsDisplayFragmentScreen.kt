package com.makentoshe.habrachan.navigation.comments

import com.makentoshe.habrachan.common.entity.comment.Comment
import com.makentoshe.habrachan.navigation.Screen
import com.makentoshe.habrachan.view.comments.CommentsDisplayFragment

class CommentsDisplayFragmentScreen(
    private val articleId: Int,
    private val commentActionEnabled: Boolean,
    private val comments: List<Comment>
) : Screen() {

    override fun getFragment(): CommentsDisplayFragment {
        val commentsFragment = CommentsDisplayFragment()
        val arguments = CommentsDisplayFragmentArguments(commentsFragment)
        arguments.articleId = articleId
        arguments.commentActionEnabled = commentActionEnabled
        arguments.comments = comments
        return commentsFragment
    }
}

