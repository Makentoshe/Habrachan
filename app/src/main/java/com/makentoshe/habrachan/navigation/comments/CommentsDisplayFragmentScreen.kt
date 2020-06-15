package com.makentoshe.habrachan.navigation.comments

import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.navigation.Screen
import com.makentoshe.habrachan.view.comments.CommentsDisplayFragment

class CommentsDisplayFragmentScreen(
    private val articleId: Int,
    private val article: Article?,
    private val commentActionEnabled: Boolean
) : Screen() {

    override fun getFragment(): CommentsDisplayFragment {
        val commentsFragment = CommentsDisplayFragment()
        val arguments = CommentsDisplayFragmentArguments(commentsFragment)
        arguments.articleId = articleId
        arguments.article = article
        arguments.commentActionEnabled = commentActionEnabled
        return commentsFragment
    }
}

