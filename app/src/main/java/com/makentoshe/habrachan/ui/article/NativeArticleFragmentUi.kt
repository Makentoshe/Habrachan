package com.makentoshe.habrachan.ui.article

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makentoshe.habrachan.R

class NativeArticleFragmentUi(private val root: ViewGroup?) {
    fun createView(context: Context): View {
        return LayoutInflater.from(context).inflate(R.layout.article_fragment_native, root, false)
    }
}