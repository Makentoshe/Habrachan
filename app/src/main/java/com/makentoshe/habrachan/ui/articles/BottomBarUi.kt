package com.makentoshe.habrachan.ui.articles

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makentoshe.habrachan.R

class BottomBarUi(private val rootView: View? = null) {
    fun createView(context: Context) : View {
        val viewGroup = if (rootView is ViewGroup) rootView else null
        return LayoutInflater.from(context).inflate(R.layout.post_fragment_bottombar, viewGroup, false)
    }
}