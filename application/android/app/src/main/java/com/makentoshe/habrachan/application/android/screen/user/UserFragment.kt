package com.makentoshe.habrachan.application.android.screen.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.CoreFragment
import com.makentoshe.habrachan.application.android.screen.user.model.UserAccount
import com.makentoshe.habrachan.application.android.screen.user.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.fragment_user.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import toothpick.ktp.delegate.inject

class UserFragment : CoreFragment() {

    companion object {

        fun build(account: UserAccount) = UserFragment().apply {
            arguments.account = account
        }
    }

    override val arguments = Arguments(this)
    private val viewModel by inject<UserViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (savedInstanceState == null) lifecycleScope.launch {
            viewModel.userAccountChannel.send(arguments.account)
        }

        lifecycleScope.launch {
            viewModel.user.collectLatest { result ->
                fragment_user_progress.visibility = View.GONE
                result.fold({ user ->
                    println(user)
                },{ throwable ->
                    println(throwable)
                })
            }
        }
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