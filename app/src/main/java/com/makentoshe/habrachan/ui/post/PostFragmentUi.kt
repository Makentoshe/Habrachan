package com.makentoshe.habrachan.ui.post

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.makentoshe.habrachan.R

class PostFragmentUi {
    fun createView(context: Context): View {
        return LayoutInflater.from(context).inflate(R.layout.post_fragment, null, false)
    }
}