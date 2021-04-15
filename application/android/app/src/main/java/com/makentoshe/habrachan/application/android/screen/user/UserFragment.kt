package com.makentoshe.habrachan.application.android.screen.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.CoreFragment
import com.makentoshe.habrachan.application.android.dp2px
import com.makentoshe.habrachan.application.android.screen.user.model.UserAccount
import com.makentoshe.habrachan.application.android.screen.user.navigation.UserNavigation
import com.makentoshe.habrachan.application.android.screen.user.viewmodel.UserViewModel
import com.makentoshe.habrachan.application.android.toRoundedDrawable
import com.makentoshe.habrachan.entity.User
import com.makentoshe.habrachan.entity.timeRegistered
import com.makentoshe.habrachan.network.response.GetContentResponse
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
    private val navigation by inject<UserNavigation>()

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

        fragment_user_toolbar.setNavigationOnClickListener { navigation.back() }

        lifecycleScope.launch {
            viewModel.user.collectLatest { either ->
                fragment_user_progress.visibility = View.GONE
                either.fold({ user -> onUserSuccess(user) }, { throwable -> println(throwable) })
            }
        }

        lifecycleScope.launch {
            viewModel.avatar.collectLatest { either ->
                either.fold(::onAvatarSuccess, ::onAvatarFailure)
            }
        }
    }

    private fun onUserSuccess(user: User) {
        fragment_user_toolbar.title = user.login
        fragment_user_fullname.text = user.fullname
        fragment_user_registered.text = SimpleDateFormat("MMMM dd, YYYY").format(user.timeRegistered)

        fragment_user_counters_karma.text = user.score.toString()
        fragment_user_counters_rating.text = user.rating.toString()
        fragment_user_counters_followers.text = user.followersCount.toString()
        fragment_user_counters_following.text = user.followsCount.toString()
    }

    private fun onAvatarSuccess(response: GetContentResponse) {
        val drawable = response.bytes.toRoundedDrawable(resources, dp2px(R.dimen.radiusS))
        fragment_user_avatar.setImageDrawable(drawable)
    }

    private fun onAvatarFailure(throwable: Throwable) {
        fragment_user_avatar.setImageResource(R.drawable.ic_account_stub)
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