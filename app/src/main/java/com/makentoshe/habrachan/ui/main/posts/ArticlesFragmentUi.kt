package com.makentoshe.habrachan.ui.main.posts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makentoshe.habrachan.R

class ArticlesFragmentUi(private val root: ViewGroup?) {
    fun createView(inflater: LayoutInflater): View {
        return inflater.inflate(R.layout.articles_fragment, root, false)
    }
}