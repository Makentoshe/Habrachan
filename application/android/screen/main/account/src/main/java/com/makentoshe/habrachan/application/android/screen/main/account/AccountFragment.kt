package com.makentoshe.habrachan.application.android.screen.main.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.makentoshe.habrachan.application.android.CoreFragment
import com.makentoshe.habrachan.application.android.FragmentArguments

class AccountFragment : CoreFragment() {

    companion object {
        fun build(): AccountFragment {
            val fragment = AccountFragment()
            return fragment
        }
    }

    val arguments = Arguments(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return TextView(requireContext()).apply { text = "Account fragment" }
    }

    class Arguments(fragment: AccountFragment) : FragmentArguments<AccountFragment>(fragment) {

    }
}

