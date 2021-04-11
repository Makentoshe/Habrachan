package com.makentoshe.habrachan.application.android.screen.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.makentoshe.habrachan.application.android.CoreFragment
import com.makentoshe.habrachan.application.android.screen.user.model.UserAccount

class UserFragment : CoreFragment() {

    companion object {

        fun build(account: UserAccount) = UserFragment().apply {
            arguments.account = account
        }
    }

    override val arguments = Arguments(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return TextView(requireContext()).apply { text = arguments.account.toString() }
    }

    class Arguments(fragment: UserFragment) : CoreFragment.Arguments(fragment) {

        var account: UserAccount
            set(value) = fragmentArguments.putSerializable(ACCOUNT, value)
            get() = fragmentArguments.getSerializable(ACCOUNT) as UserAccount

        companion object {
            private const val ACCOUNT = "UserAccount"
        }
    }
}