package com.makentoshe.habrachan.application.android.screen.article.navigation

import com.makentoshe.habrachan.application.android.screen.article.ArticleFragment
import com.makentoshe.habrachan.entity.ArticleId
import ru.terrakok.cicerone.android.support.SupportAppScreen

class ArticleScreen2(val articleId: ArticleId) : SupportAppScreen() {

    override fun getFragment() = ArticleFragment.build(articleId)
}