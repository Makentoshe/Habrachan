package com.makentoshe.habrachan.application.android.screen.article.navigation

import com.makentoshe.habrachan.application.android.screen.article.ArticleFragment
import com.makentoshe.habrachan.entity.Article
import com.makentoshe.habrachan.entity.ArticleId
import ru.terrakok.cicerone.android.support.SupportAppScreen

class ArticleScreen(private val articleId: ArticleId, private val article: Article? = null) : SupportAppScreen() {

    constructor(article: Article): this(article, article)

    init {
        if (article != null && article != articleId) throw IllegalArgumentException()
    }

    override fun getFragment() = ArticleFragment.build(articleId)
}