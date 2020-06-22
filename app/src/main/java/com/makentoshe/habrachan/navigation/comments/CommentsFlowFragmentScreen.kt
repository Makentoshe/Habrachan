package com.makentoshe.habrachan.navigation.comments

import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.common.entity.comment.Comment
import com.makentoshe.habrachan.navigation.Screen
import com.makentoshe.habrachan.view.comments.CommentsFlowFragment

class CommentsFlowFragmentScreen private constructor(
    private val articleId: Int,
    private val article: Article?,
    private val commentActionsEnabled: Boolean,
    private val comments: List<Comment>?
) : Screen() {

    constructor(articleId: Int, article: Article?, commentActionsEnabled: Boolean) : this(
        articleId,
        article,
        commentActionsEnabled,
        null
    )

    constructor(article: Article, commentActionsEnabled: Boolean) : this(
        article.id,
        article,
        commentActionsEnabled,
        null
    )

    constructor(articleId: Int, comments: List<Comment>?, commentActionsEnabled: Boolean) : this(
        articleId,
        null,
        commentActionsEnabled,
        comments
    )

    override fun getFragment(): CommentsFlowFragment {
        val fragment = CommentsFlowFragment()
        val arguments = CommentsFlowFragmentArguments(fragment)
        arguments.article = article
        arguments.articleId = articleId
        arguments.commentActionsEnabled = commentActionsEnabled
        arguments.comments = comments
        return fragment
    }
}