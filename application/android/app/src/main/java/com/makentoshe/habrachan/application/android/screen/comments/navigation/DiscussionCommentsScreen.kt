package com.makentoshe.habrachan.application.android.screen.comments.navigation

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.application.android.screen.comments.DiscussionCommentsFragment
import com.makentoshe.habrachan.entity.natives.Article
import ru.terrakok.cicerone.android.support.SupportAppScreen

class DiscussionCommentsScreen(
    val articleId: Int,
    val articleTitle: String,
    val commentId: Int = 0
) : SupportAppScreen() {

    constructor(article: Article, commentId: Int = 0): this(article.id, article.title, commentId)

    override fun getFragment(): Fragment {
        return DiscussionCommentsFragment.build(articleId, articleTitle, commentId)
    }
}