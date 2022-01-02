package com.makentoshe.habrachan.application.android.screen.articles.navigation.navigator

import com.makentoshe.habrachan.entity.article.component.ArticleId


interface ArticleScreenNavigator {

    fun toArticleScreen(articleId: ArticleId)
}