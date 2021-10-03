package com.makentoshe.habrachan.application.android.screen.articles.navigation.navigator

import com.makentoshe.habrachan.entity.ArticleId
import com.makentoshe.habrachan.functional.Option

interface ArticleCommentsScreenNavigator {
    fun toArticleCommentsScreen(articleId: ArticleId, articleTitle: Option<String> = Option.None)
}