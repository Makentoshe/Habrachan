package com.makentoshe.habrachan.application.android.screen.articles.navigation

import com.makentoshe.habrachan.application.android.navigation.StackRouter
import com.makentoshe.habrachan.application.android.screen.article.navigation.ArticleScreen
import com.makentoshe.habrachan.application.android.screen.login.navigation.MobileLoginScreen
import com.makentoshe.habrachan.application.android.screen.user.model.UserAccount
import com.makentoshe.habrachan.application.android.screen.user.navigation.UserScreen
import com.makentoshe.habrachan.entity.Article

class ArticlesNavigation(private val router: StackRouter) {

    fun navigateToArticle(article: Article) {
        router.stack(ArticleScreen(article))
    }

    fun navigateToLogin() {
        router.stack(MobileLoginScreen())
    }

    fun navigateToUser() {
        router.stack(UserScreen(UserAccount.Me()))
    }
}