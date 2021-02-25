package com.makentoshe.habrachan.application.android.screen.articles.navigation

import com.makentoshe.habrachan.application.android.screen.articles.ArticlesFlowFragment
import com.makentoshe.habrachan.network.request.GetArticlesRequest
import ru.terrakok.cicerone.android.support.SupportAppScreen

class ArticlesFlowScreen : SupportAppScreen() {
    override fun getFragment() = ArticlesFlowFragment.build(
        listOf(
            GetArticlesRequest.Spec.All(),
            GetArticlesRequest.Spec.Interesting(),
            GetArticlesRequest.Spec.Top(GetArticlesRequest.Spec.Top.Type.Daily)
        )
    )
}