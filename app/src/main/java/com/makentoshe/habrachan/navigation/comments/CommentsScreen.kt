package com.makentoshe.habrachan.navigation.comments

import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.navigation.Screen
import com.makentoshe.habrachan.view.comments.CommentsFlowFragment
import com.makentoshe.habrachan.view.comments.CommentsFragment

class CommentsScreen(private val articleId: Int, private val article: Article? = null) : Screen() {

    constructor(article: Article) : this(article.id, article)

    override fun getFragment() = CommentsFlowFragment.Factory().build(articleId, article)
}