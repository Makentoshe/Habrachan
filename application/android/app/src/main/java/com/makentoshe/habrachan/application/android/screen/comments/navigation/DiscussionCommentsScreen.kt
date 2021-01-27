package com.makentoshe.habrachan.application.android.screen.comments.navigation

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.application.android.screen.comments.DiscussionCommentsFragment
import com.makentoshe.habrachan.entity.Article
import ru.terrakok.cicerone.android.support.SupportAppScreen

class DiscussionCommentsScreen(
    private val articleId: Int,
    private val articleTitle: String,
    private val commentId: Int = 0
) : SupportAppScreen() {

    constructor(article: Article, commentId: Int = 0): this(article.id, article.title, commentId)

    override fun getFragment(): Fragment {
        return DiscussionCommentsFragment.build(articleId, articleTitle, commentId)
    }
}