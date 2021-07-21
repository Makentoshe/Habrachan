package com.makentoshe.habrachan.application.android.screen.comments.navigation

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.application.android.screen.comments.ArticleCommentsFragment
import com.makentoshe.habrachan.entity.Article
import com.makentoshe.habrachan.entity.ArticleId
import ru.terrakok.cicerone.android.support.SupportAppScreen

class ArticleCommentsScreen(
    private val articleId: ArticleId,
    private val articleTitle: String
) : SupportAppScreen() {

    constructor(article: Article): this(article, article.title)

    override fun getFragment(): Fragment {
        return ArticleCommentsFragment.build(articleId, articleTitle)
    }
}

