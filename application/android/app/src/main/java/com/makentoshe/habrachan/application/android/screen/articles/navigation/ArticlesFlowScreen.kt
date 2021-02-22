package com.makentoshe.habrachan.application.android.screen.articles.navigation

import com.makentoshe.habrachan.application.android.screen.articles.ArticlesFlowFragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

class ArticlesFlowScreen(): SupportAppScreen() {
    override fun getFragment() = ArticlesFlowFragment.build()
}