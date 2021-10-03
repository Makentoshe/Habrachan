package com.makentoshe.habrachan.application.android.screen.comments.articles.navigation

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.application.android.screen.comments.articles.ArticleCommentsFragment
import com.makentoshe.habrachan.entity.Article
import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.functional.Option
import ru.terrakok.cicerone.android.support.SupportAppScreen

class ArticleCommentsScreen(
    val articleId: ArticleId,
    val articleTitleOption: Option<String>
) : SupportAppScreen() {

    constructor(article: Article) : this(article, Option.from(article.title))

    override fun getFragment(): Fragment {
        return ArticleCommentsFragment.build(articleId, articleTitleOption)
    }
}

