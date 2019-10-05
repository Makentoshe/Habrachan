package com.makentoshe.habrachan.ui.posts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.makentoshe.habrachan.R

class PostsPageFragmentUi {

    fun createView(context: Context): View {
        return LayoutInflater.from(context).inflate(R.layout.main_posts_page_fragment, null, false)
    }
}