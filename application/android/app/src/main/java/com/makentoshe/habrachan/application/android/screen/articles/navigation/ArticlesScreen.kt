package com.makentoshe.habrachan.application.android.screen.articles.navigation

import com.makentoshe.habrachan.application.android.screen.articles.ArticlesFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class ArticlesScreen(private val page: Int) : SupportAppScreen() {
    override fun getFragment() = ArticlesFragment.build(page)
}