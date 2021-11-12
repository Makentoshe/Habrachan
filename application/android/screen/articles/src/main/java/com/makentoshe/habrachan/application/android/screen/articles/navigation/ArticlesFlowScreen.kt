package com.makentoshe.habrachan.application.android.screen.articles.navigation

import com.makentoshe.habrachan.application.android.screen.articles.ArticlesFlowFragment
import com.makentoshe.habrachan.application.android.screen.articles.model.ArticlesUserSearch
import ru.terrakok.cicerone.android.support.SupportAppScreen

class ArticlesFlowScreen(val articlesUserSearches : List<ArticlesUserSearch>) : SupportAppScreen() {
    override fun getFragment() = ArticlesFlowFragment.build(articlesUserSearches)
}