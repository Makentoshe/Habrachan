package com.makentoshe.habrachan.ui.articles.comments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makentoshe.habrachan.R

class CommentsFragmentUi(private val root: View? = null) {
    fun createView(context: Context): View {
        val rootGroup = if (root is ViewGroup) root else null
        return LayoutInflater.from(context).inflate(R.layout.comments_fragment, rootGroup, false)
    }
}