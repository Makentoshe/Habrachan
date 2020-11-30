package com.makentoshe.habrachan.application.android.screen.article.navigation

import com.makentoshe.habrachan.application.android.screen.article.ArticleFragment
import com.makentoshe.habrachan.navigation.Screen

class ArticleScreen(private val articleId: Int) : Screen() {
    override fun getFragment() = ArticleFragment.build(articleId)
}