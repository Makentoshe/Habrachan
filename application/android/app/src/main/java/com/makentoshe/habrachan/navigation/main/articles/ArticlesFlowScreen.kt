package com.makentoshe.habrachan.navigation.main.articles

import com.makentoshe.habrachan.navigation.Screen
import com.makentoshe.habrachan.view.main.articles.ArticlesFlowFragment

class ArticlesFlowScreen(private val page: Int) : Screen() {
    override fun getFragment() = ArticlesFlowFragment.Factory().build(page)
}