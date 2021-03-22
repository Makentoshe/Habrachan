package com.makentoshe.habrachan.application.android.screen.articles.navigation

import com.makentoshe.habrachan.application.android.navigation.StackRouter
import com.makentoshe.habrachan.application.android.screen.article.navigation.ArticleScreen
import com.makentoshe.habrachan.application.android.screen.login.navigation.LoginScreen
import com.makentoshe.habrachan.entity.Article
import ru.terrakok.cicerone.Router

class ArticlesNavigation(private val router: StackRouter) {

    fun navigateToArticle(article: Article) {
        router.stack(ArticleScreen(article))
    }

    fun navigateToLogin() {
        router.stack(LoginScreen())
    }
}