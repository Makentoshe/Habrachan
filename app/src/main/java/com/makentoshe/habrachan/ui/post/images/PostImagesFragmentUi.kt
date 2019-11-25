package com.makentoshe.habrachan.ui.post.images

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.makentoshe.habrachan.R

class PostImagesFragmentUi {
    fun createView(context: Context): View {
        return LayoutInflater.from(context).inflate(R.layout.post_images_fragment, null, false)
    }
}