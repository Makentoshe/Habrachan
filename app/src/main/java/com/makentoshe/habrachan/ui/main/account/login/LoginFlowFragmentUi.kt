package com.makentoshe.habrachan.ui.main.account.login

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makentoshe.habrachan.R

class LoginFlowFragmentUi(private val root: ViewGroup?) {
    fun createView(context: Context): View {
        return LayoutInflater.from(context).inflate(R.layout.login_flow_fragment, root, false)
    }
}