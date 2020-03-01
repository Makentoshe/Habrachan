package com.makentoshe.habrachan.ui.articles

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.makentoshe.habrachan.R

class PostFragmentUi {
    fun createView(context: Context): View {
        return LayoutInflater.from(context).inflate(R.layout.article_fragment, null, false)
    }
}