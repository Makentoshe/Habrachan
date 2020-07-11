package com.makentoshe.habrachan.ui.main.account.login

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makentoshe.habrachan.R

class OauthFragmentUi(private val root: ViewGroup?) {
    fun inflateView(inflater: LayoutInflater): View {
        return inflater.inflate(R.layout.oauth_fragment, root, false)
    }
}