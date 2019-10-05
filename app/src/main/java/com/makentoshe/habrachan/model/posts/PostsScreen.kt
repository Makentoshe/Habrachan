package com.makentoshe.habrachan.model.posts

import androidx.fragment.app.Fragment
import com.makentoshe.habrachan.common.model.navigation.Screen
import com.makentoshe.habrachan.view.posts.PostsFragment

class PostsScreen : Screen(){
    override val fragment: Fragment
        get() = PostsFragment()
}