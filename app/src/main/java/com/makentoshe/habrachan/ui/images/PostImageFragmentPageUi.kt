package com.makentoshe.habrachan.ui.images

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.makentoshe.habrachan.R

class PostImageFragmentPageUi {
    fun createView(context: Context): View {
        return LayoutInflater.from(context).inflate(R.layout.post_image_fragment, null, false)
    }
}