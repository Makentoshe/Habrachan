package com.makentoshe.habrachan.ui.main.account

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.makentoshe.habrachan.R

class AccountFlowFragmentUi {
    fun create(context: Context): View {
        return LayoutInflater.from(context).inflate(R.layout.account_flow_fragment, null, false)
    }
}