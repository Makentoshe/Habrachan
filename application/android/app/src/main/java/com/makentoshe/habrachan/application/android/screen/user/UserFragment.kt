package com.makentoshe.habrachan.application.android.screen.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.makentoshe.habrachan.application.android.CoreFragment

class UserFragment : CoreFragment() {

    companion object {

        fun build() = UserFragment().apply {

        }
    }

    override val arguments = Arguments(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return TextView(requireContext()).apply { text = "UserScreen" }
    }

    class Arguments(fragment: UserFragment) : CoreFragment.Arguments(fragment) {

    }
}