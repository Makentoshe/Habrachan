package com.makentoshe.habrachan.ui.comments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makentoshe.habrachan.R

class CommentsFlowFragmentUi(private val root: ViewGroup? = null) {

    fun inflateView(inflater: LayoutInflater): View {
        return inflater.inflate(R.layout.comments_flow_fragment, root, false)
    }
}