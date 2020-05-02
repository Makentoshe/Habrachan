package com.makentoshe.habrachan.model.article

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.common.navigation.Screen
import com.makentoshe.habrachan.view.article.ArticleFragment

class NativeArticleScreen(private val articleId: Int): Screen() {
    override val fragment: Fragment
        get() = ArticleFragment.Factory().buildNative(articleId)
}