package com.makentoshe.habrachan.ui.commentinput

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makentoshe.habrachan.R

class CommentInputFragmentUi(private val root: ViewGroup? = null) {
    fun inflateView(inflater: LayoutInflater): View {
        return inflater.inflate(R.layout.commentinput_fragment, root, false)
    }
}