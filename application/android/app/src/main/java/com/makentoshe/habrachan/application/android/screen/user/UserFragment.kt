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
import com.makentoshe.habrachan.entity.User
import com.makentoshe.habrachan.entity.timeRegistered
import kotlinx.android.synthetic.main.fragment_user.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import toothpick.ktp.delegate.inject
import java.text.SimpleDateFormat

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

        when (val account = arguments.account) {
            is UserAccount.Me -> fragment_user_toolbar.title = account.login
        }

        lifecycleScope.launch {
            viewModel.user.collectLatest { either ->
                fragment_user_progress.visibility = View.GONE
                either.fold({ user -> onUserSuccess(user) },{ throwable -> println(throwable) })
            }
        }
    }

    private fun onUserSuccess(user: User) {
        fragment_user_toolbar.title = user.login
        fragment_user_fullname.text = user.fullname
        fragment_user_registered.text = SimpleDateFormat("MMMM dd, YYYY").format(user.timeRegistered)
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