package com.makentoshe.habrachan.application.android.navigation

import com.makentoshe.habrachan.application.android.screen.articles.navigation.StackRouter
import com.makentoshe.habrachan.application.android.screen.articles.navigation.navigator.ArticleCommentsScreenNavigator
import com.makentoshe.habrachan.application.android.screen.comments.navigation.ArticleCommentsScreen
import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.functional.Option
import javax.inject.Inject

class ArticleCommentsScreenNavigatorImpl @Inject constructor(
    private val router: StackRouter
) : ArticleCommentsScreenNavigator {
    override fun toArticleCommentsScreen(articleId: ArticleId, articleTitle: Option<String>) {
        router.navigateTo(ArticleCommentsScreen(articleId, articleTitle))
    }
}