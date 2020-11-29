package com.makentoshe.habrachan.ui.user

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makentoshe.habrachan.R

class UserProfileFragmentUi(private val root: ViewGroup?) {
    fun create(context: Context): View {
        return LayoutInflater.from(context).inflate(R.layout.user_fragment_content_profile, root, false)
    }
}

