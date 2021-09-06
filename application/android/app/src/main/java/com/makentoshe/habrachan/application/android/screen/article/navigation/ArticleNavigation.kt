package com.makentoshe.habrachan.application.android.screen.article.navigation

import com.makentoshe.habrachan.application.android.navigation.StackRouter
import com.makentoshe.habrachan.application.android.screen.comments.articles.navigation.ArticleCommentsScreen
import com.makentoshe.habrachan.application.android.screen.content.navigation.ContentScreen
import com.makentoshe.habrachan.application.android.screen.login.navigation.LoginScreen
import com.makentoshe.habrachan.application.android.screen.user.model.UserAccount
import com.makentoshe.habrachan.application.android.screen.user.navigation.UserScreen
import com.makentoshe.habrachan.entity.Article

class ArticleNavigation(private val router: StackRouter) {

    /** Returns to MainScreen */
    fun back() {
        router.exit()
    }

    fun toArticleCommentsScreen(article: Article) {
        router.stack(ArticleCommentsScreen(article))
    }

    fun toArticleContentScreen(source: String) {
        router.stack(ContentScreen(source))
    }

    fun navigateToUserScreen(login: String) {
        router.stack(UserScreen(UserAccount.User(login)))
    }

    fun navigateToLoginScreen() {
        router.stack(LoginScreen())
    }
}