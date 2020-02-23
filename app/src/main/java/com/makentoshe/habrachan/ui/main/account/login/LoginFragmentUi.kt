package com.makentoshe.habrachan.ui.main.account.login

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.makentoshe.habrachan.R

class LoginFragmentUi {
    fun createView(context: Context): View {
        return LayoutInflater.from(context).inflate(R.layout.login_fragment, null, false)
    }
}