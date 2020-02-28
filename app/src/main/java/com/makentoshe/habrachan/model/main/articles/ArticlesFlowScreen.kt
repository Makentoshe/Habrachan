package com.makentoshe.habrachan.model.main.articles

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.common.navigation.Screen
import com.makentoshe.habrachan.view.main.articles.ArticlesFlowFragment

class ArticlesFlowScreen(private val page: Int) : Screen(){
    override val fragment: Fragment
        get() = ArticlesFlowFragment.Factory().build(page)
}