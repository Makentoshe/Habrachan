package com.makentoshe.habrachan.navigation.comments

import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.navigation.Screen
import com.makentoshe.habrachan.view.comments.CommentsFlowFragment

class CommentsFlowFragmentScreen(
    private val articleId: Int,
    private val article: Article?,
    private val commentActionsEnabled: Boolean
) : Screen() {

    constructor(article: Article, enableCommentActions: Boolean) : this(article.id, article,enableCommentActions)

    override fun getFragment(): CommentsFlowFragment {
        val fragment = CommentsFlowFragment()
        val arguments = CommentsFlowFragmentArguments(fragment)
        arguments.article = article
        arguments.articleId = articleId
        arguments.commentActionsEnabled = commentActionsEnabled
        return fragment
    }
}