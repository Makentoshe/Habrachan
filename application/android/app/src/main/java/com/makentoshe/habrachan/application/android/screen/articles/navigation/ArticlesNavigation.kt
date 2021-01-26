package com.makentoshe.habrachan.application.android.screen.articles.navigation

import com.makentoshe.habrachan.application.android.screen.article.navigation.ArticleScreen
import com.makentoshe.habrachan.entity.Article
import ru.terrakok.cicerone.Router

class ArticlesNavigation(private val router: Router) {

    fun navigateToArticle(article: Article) {
        router.navigateTo(ArticleScreen(article))
    }
}