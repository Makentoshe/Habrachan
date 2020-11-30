package com.makentoshe.habrachan.application.android.screen.articles.navigation

import com.makentoshe.habrachan.application.android.screen.articles.ArticlesFlowFragment
import com.makentoshe.habrachan.navigation.Screen

class ArticlesFlowScreen(private val page: Int) : Screen() {
    override fun getFragment() = ArticlesFlowFragment.build(page)
}