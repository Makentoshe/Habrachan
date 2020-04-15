package com.makentoshe.habrachan.ui.main.menu

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makentoshe.habrachan.R

class MenuFragmentUi(private val root: ViewGroup?) {
    fun createView(context: Context): View {
        return LayoutInflater.from(context).inflate(R.layout.menu_fragment, root, false)
    }
}
