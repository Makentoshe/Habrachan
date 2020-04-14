package com.makentoshe.habrachan.ui.main.articles

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makentoshe.habrachan.R

class ArticlesSearchFragmentUi(private val root: ViewGroup?) {
    fun buildView(context: Context): View {
        return LayoutInflater.from(context).inflate(R.layout.articles_search_fragment, root, false)
    }
}