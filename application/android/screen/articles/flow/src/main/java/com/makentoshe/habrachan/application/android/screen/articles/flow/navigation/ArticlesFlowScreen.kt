package com.makentoshe.habrachan.application.android.screen.articles.flow.navigation

import com.makentoshe.habrachan.application.android.screen.articles.flow.ArticlesFlowFragment
import com.makentoshe.habrachan.network.request.SpecType
import com.makentoshe.habrachan.network.request.TopSpecType
import ru.terrakok.cicerone.android.support.SupportAppScreen

class ArticlesFlowScreen : SupportAppScreen() {
    override fun getFragment() = ArticlesFlowFragment.build(
        listOf(SpecType.All, SpecType.Interesting, SpecType.Top(TopSpecType.Daily))
    )
}