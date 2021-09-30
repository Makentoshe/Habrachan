package com.makentoshe.habrachan.application.android.screen.articles.navigation.navigator

import com.makentoshe.habrachan.entity.ArticleId

interface ArticleScreenNavigator {

    fun toArticleScreen(articleId: ArticleId)
}