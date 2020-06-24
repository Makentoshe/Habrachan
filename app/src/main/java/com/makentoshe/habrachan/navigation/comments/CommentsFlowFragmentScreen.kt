package com.makentoshe.habrachan.navigation.comments

import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.common.entity.comment.Comment
import com.makentoshe.habrachan.navigation.Screen
import com.makentoshe.habrachan.view.comments.CommentsFlowFragment

class CommentsFlowFragmentScreen private constructor(
    private val articleId: Int,
    private val article: Article?,
    private val commentActionsEnabled: Boolean,
    private val comments: List<Comment>?,
    private val parentId: Int
) : Screen() {

    constructor(articleId: Int, article: Article?, commentActionsEnabled: Boolean) : this(
        articleId,
        article,
        commentActionsEnabled,
        null,
        0
    )

    constructor(article: Article, commentActionsEnabled: Boolean) : this(
        article.id,
        article,
        commentActionsEnabled,
        null,
        0
    )

    constructor(articleId: Int, comments: List<Comment>?, commentActionsEnabled: Boolean, parentId: Int) : this(
        articleId,
        null,
        commentActionsEnabled,
        comments,
        parentId
    )

    override fun getFragment(): CommentsFlowFragment {
        val fragment = CommentsFlowFragment()
        val arguments = CommentsFlowFragmentArguments(fragment)
        arguments.article = article
        arguments.articleId = articleId
        arguments.commentActionsEnabled = commentActionsEnabled
        arguments.comments = comments
        arguments.parentId = parentId
        return fragment
    }
}