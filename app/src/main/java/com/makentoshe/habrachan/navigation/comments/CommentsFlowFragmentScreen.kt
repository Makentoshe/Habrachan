package com.makentoshe.habrachan.navigation.comments

import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.navigation.Screen
import com.makentoshe.habrachan.view.comments.CommentsFlowFragment

class CommentsFlowFragmentScreen(
    private val articleId: Int,
    private val article: Article?
) : Screen() {

    constructor(article: Article) : this(article.id, article)

    override fun getFragment(): CommentsFlowFragment {
        val fragment = CommentsFlowFragment()
        val arguments = CommentsFlowFragmentArguments(fragment)
        arguments.article = article
        arguments.articleId = articleId
        return fragment
    }
}