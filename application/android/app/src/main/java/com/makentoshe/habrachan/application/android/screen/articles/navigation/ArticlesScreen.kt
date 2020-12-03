package com.makentoshe.habrachan.application.android.screen.articles.navigation

import com.makentoshe.habrachan.application.android.screen.articles.ArticlesFragment2
import com.makentoshe.habrachan.navigation.Screen

class ArticlesScreen(private val page: Int) : Screen() {
    override fun getFragment() = ArticlesFragment2.build(page)
}