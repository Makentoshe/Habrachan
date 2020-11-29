package com.makentoshe.habrachan.ui.user

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.makentoshe.habrachan.R

class UserFragmentUi {
    fun create(context: Context): View {
        return LayoutInflater.from(context).inflate(R.layout.user_fragment, null, false)
    }
}