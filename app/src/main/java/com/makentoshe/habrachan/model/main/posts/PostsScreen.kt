package com.makentoshe.habrachan.model.main.posts

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.common.navigation.Screen
import com.makentoshe.habrachan.view.main.posts.PostsFragment

class PostsScreen(private val page: Int) : Screen(){
    override val fragment: Fragment
        get() = PostsFragment.Factory().build(page)
}