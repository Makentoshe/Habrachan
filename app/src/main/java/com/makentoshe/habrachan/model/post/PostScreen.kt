package com.makentoshe.habrachan.model.post

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.common.navigation.Screen
import com.makentoshe.habrachan.view.post.ArticleFragment

class PostScreen(private val postId: Int) : Screen(){
    override val fragment: Fragment
        get() = ArticleFragment.Factory().build(postId)
}