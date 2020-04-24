package com.makentoshe.habrachan.ui.user

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makentoshe.habrachan.R

class UserArticlesFragmentUi(private val root: ViewGroup?) {
    fun createView(context: Context): View {
        return LayoutInflater.from(context).inflate(R.layout.user_fragment_content_articles, root, false)
    }
}