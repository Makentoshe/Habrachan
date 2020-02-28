package com.makentoshe.habrachan.ui.main.posts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makentoshe.habrachan.R

class ArticlesFlowFragmentUi(private val root: ViewGroup?) {
    fun createView(layoutInflater: LayoutInflater): View {
        return layoutInflater.inflate(R.layout.articles_flow_fragment, root, false)
    }
}
