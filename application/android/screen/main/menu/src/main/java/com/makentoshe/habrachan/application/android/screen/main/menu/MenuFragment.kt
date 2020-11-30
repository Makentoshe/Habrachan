package com.makentoshe.habrachan.application.android.screen.main.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.makentoshe.habrachan.application.android.CoreFragment

class MenuFragment : CoreFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return TextView(requireContext()).apply { text = "Menu fragment" }
    }
}