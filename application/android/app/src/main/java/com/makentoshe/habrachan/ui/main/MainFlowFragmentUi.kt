package com.makentoshe.habrachan.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.makentoshe.habrachan.R

class MainFlowFragmentUi {
    fun createView(context: Context): View {
        return LayoutInflater.from(context).inflate(R.layout.main_flow_fragment, null, false)
    }
}