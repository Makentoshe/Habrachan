package com.makentoshe.habrachan.ui.main.account.user

import android.content.Context
import android.view.View
import android.widget.TextView

class UserFragmentUi {
    fun create(context: Context): View {
        return TextView(context).apply {
            text = "USER FRAGMENT"
        }
//        return LayoutInflater.from(context).inflate()
    }
}