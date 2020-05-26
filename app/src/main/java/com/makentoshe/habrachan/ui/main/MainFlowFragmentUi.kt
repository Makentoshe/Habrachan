package com.makentoshe.habrachan.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makentoshe.habrachan.R

class MainFlowFragmentUi(private val root: ViewGroup? = null) {
    fun inflateView(inflater: LayoutInflater): View {
        return inflater.inflate(R.layout.main_flow_fragment, root, false)
    }
}