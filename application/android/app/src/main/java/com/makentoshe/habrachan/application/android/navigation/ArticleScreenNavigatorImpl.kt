package com.makentoshe.habrachan.application.android.navigation

import com.makentoshe.habrachan.application.android.screen.article.navigation.ArticleScreen2
import com.makentoshe.habrachan.application.android.screen.articles.navigation.StackRouter
import com.makentoshe.habrachan.application.android.screen.articles.navigation.navigator.ArticleScreenNavigator
import com.makentoshe.habrachan.entity.article.component.ArticleId
import javax.inject.Inject

class ArticleScreenNavigatorImpl @Inject constructor(
    private val router: StackRouter,
) : ArticleScreenNavigator {
    override fun toArticleScreen(articleId: ArticleId) {
        router.navigateTo(ArticleScreen2(articleId))
    }
}