package com.makentoshe.habrachan.ui.main

import android.content.Context
import android.view.View
import android.widget.TextView

class MainFlowFragmentUi {
    fun createView(context: Context): View {
        return TextView(context).apply {
            text = "Main Flow Fragment"
        }
    }
}