package com.makentoshe.habrachan.model.post

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.common.navigation.Screen
import com.makentoshe.habrachan.view.post.PostFragment

class PostScreen(private val page: Int,private val position: Int) : Screen(){
    override val fragment: Fragment
        get() = PostFragment.Factory().build(page, position)
}