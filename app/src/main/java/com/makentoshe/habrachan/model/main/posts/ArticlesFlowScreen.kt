package com.makentoshe.habrachan.model.main.posts

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.common.navigation.Screen
import com.makentoshe.habrachan.view.main.posts.ArticlesFlowFragment
import com.makentoshe.habrachan.view.main.posts.ArticlesFragment

class ArticlesFlowScreen(private val page: Int) : Screen(){
    override val fragment: Fragment
        get() = ArticlesFlowFragment.Factory().build(page)
}