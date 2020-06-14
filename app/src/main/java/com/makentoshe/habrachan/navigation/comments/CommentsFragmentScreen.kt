package com.makentoshe.habrachan.navigation.comments

import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.navigation.Screen
import com.makentoshe.habrachan.view.comments.CommentsFragment

class CommentsFragmentScreen(
    private val articleId: Int,
    private val article: Article?,
    private val commentActionEnabled: Boolean
) : Screen() {

    override fun getFragment(): CommentsFragment {
        val commentsFragment = CommentsFragment()
        val arguments = CommentsFragmentArguments(commentsFragment)
        arguments.articleId = articleId
        arguments.article = article
        arguments.commentActionEnabled = commentActionEnabled
        return commentsFragment
    }
}

