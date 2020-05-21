package com.makentoshe.habrachan.navigation.article

import com.makentoshe.habrachan.navigation.Screen
import com.makentoshe.habrachan.view.article.ArticleFragment

class NativeArticleScreen(private val articleId: Int) : Screen() {
    override fun getFragment() = ArticleFragment.Factory().buildNative(articleId)
}