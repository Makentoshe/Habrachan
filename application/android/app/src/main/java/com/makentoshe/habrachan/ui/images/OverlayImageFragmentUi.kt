package com.makentoshe.habrachan.ui.images

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makentoshe.habrachan.R

class OverlayImageFragmentUi(private val root: ViewGroup?) {

    fun inflateView(inflater: LayoutInflater): View {
        return inflater.inflate(R.layout.post_image_fragment, root, false)
    }
}