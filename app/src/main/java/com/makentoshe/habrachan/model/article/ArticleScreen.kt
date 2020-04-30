package com.makentoshe.habrachan.model.article

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.common.navigation.Screen
import com.makentoshe.habrachan.view.article.ArticleFragment
import com.makentoshe.habrachan.view.article.WebArticleFragment

class WebArticleScreen(private val articleId: Int): Screen() {
    override val fragment: Fragment
        get() = ArticleFragment.Factory().buildWeb(articleId)
}