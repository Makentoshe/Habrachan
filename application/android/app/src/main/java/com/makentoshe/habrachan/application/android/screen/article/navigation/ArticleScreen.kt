package com.makentoshe.habrachan.application.android.screen.article.navigation

import com.makentoshe.habrachan.application.android.screen.article.ArticleFragment
import com.makentoshe.habrachan.entity.Article
import ru.terrakok.cicerone.android.support.SupportAppScreen

class ArticleScreen(private val articleId: Int, private val article: Article? = null) : SupportAppScreen() {

    constructor(article: Article): this(article.articleId, article)

    init {
        if (article != null && article.articleId != articleId) throw IllegalArgumentException()
    }

    override fun getFragment() = ArticleFragment.build(articleId)
}