package com.makentoshe.habrachan.model.comments

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.common.entity.Article
import com.makentoshe.habrachan.common.navigation.Screen
import com.makentoshe.habrachan.view.comments.CommentsFragment

class CommentsScreen(private val articleId: Int, private val article: Article? = null) : Screen() {

    constructor(article: Article): this(article.id, article)

    override val fragment: Fragment
        get() = CommentsFragment.Factory().build(articleId, article)
}