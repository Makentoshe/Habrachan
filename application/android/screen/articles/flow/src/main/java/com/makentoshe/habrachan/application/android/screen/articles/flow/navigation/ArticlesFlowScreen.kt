package com.makentoshe.habrachan.application.android.screen.articles.flow.navigation

import com.makentoshe.habrachan.application.android.screen.articles.flow.ArticlesFlowFragment
import com.makentoshe.habrachan.application.android.screen.articles.flow.model.ArticlesUserSearch
import ru.terrakok.cicerone.android.support.SupportAppScreen

class ArticlesFlowScreen(val articlesUserSearches : List<ArticlesUserSearch>) : SupportAppScreen() {
    override fun getFragment() = ArticlesFlowFragment.build(articlesUserSearches)
}