package com.makentoshe.habrachan.application.android.screen.article.navigation

import com.makentoshe.habrachan.application.android.screen.article.ArticleFragment
import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.functional.Option
import ru.terrakok.cicerone.android.support.SupportAppScreen

class ArticleScreen2(val articleId: ArticleId, val articleTitle: Option<String>) : SupportAppScreen() {

    constructor(articleId: ArticleId) : this(articleId, Option.None)

    constructor(articleId: ArticleId, articleTitle: String) : this(articleId, Option.from(articleTitle))

    override fun getFragment() = ArticleFragment.build(articleId, articleTitle)
}