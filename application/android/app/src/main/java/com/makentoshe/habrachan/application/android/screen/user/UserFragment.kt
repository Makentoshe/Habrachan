package com.makentoshe.habrachan.application.android.screen.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.CoreFragment
import com.makentoshe.habrachan.application.android.ExceptionController
import com.makentoshe.habrachan.application.android.ExceptionHandler
import com.makentoshe.habrachan.application.android.ExceptionViewHolder
import com.makentoshe.habrachan.application.android.analytics.Analytics
import com.makentoshe.habrachan.application.android.analytics.LogAnalytic
import com.makentoshe.habrachan.application.android.analytics.event.analyticEvent
import com.makentoshe.habrachan.application.android.common.dp2px
import com.makentoshe.habrachan.application.android.common.toRoundedDrawable
import com.makentoshe.habrachan.application.android.screen.user.model.UserAccount
import com.makentoshe.habrachan.application.android.screen.user.navigation.UserNavigation
import com.makentoshe.habrachan.application.android.screen.user.viewmodel.UserViewModel
import com.makentoshe.habrachan.entity.User
import com.makentoshe.habrachan.entity.timeRegistered
import com.makentoshe.habrachan.network.GetContentResponse
import kotlinx.android.synthetic.main.fragment_user.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import toothpick.ktp.delegate.inject
import java.text.SimpleDateFormat

class UserFragment : CoreFragment() {

    companion object : Analytics(LogAnalytic()) {

        fun build(account: UserAccount) = UserFragment().apply {
            arguments.account = account
        }

        private const val VIEW_MODEL_STATE_KEY = "ViewModel"
    }

    override val arguments = Arguments(this)
    private val viewModel by inject<UserViewModel>()
    private val navigation by inject<UserNavigation>()
    private val exceptionHandler by inject<ExceptionHandler>()

    private lateinit var exceptionController: ExceptionController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.setOnClickListener { /* workaround */ }

        val wasViewModelRecreated = viewModel.toString() != savedInstanceState?.getString(VIEW_MODEL_STATE_KEY)
        if (savedInstanceState == null || wasViewModelRecreated) lifecycleScope.launch {
            capture(analyticEvent(this@UserFragment.javaClass.simpleName, arguments.account.toString()))
            viewModel.accountChannel.send(arguments.account)
        }

        fragment_user_toolbar.setNavigationOnClickListener { navigation.back() }
        fragment_user_toolbar.setOnMenuItemClickListener(::onMenuItemClick)
        exceptionController = ExceptionController(ExceptionViewHolder(fragment_user_exception))

        lifecycleScope.launch {
            viewModel.userFlow.collectLatest { either ->
                fragment_user_progress.visibility = View.GONE
                fragment_user_avatar_progress.visibility =
                    if (fragment_user_avatar.isVisible) View.GONE else View.VISIBLE
                either.fold({ user -> onUserSuccess(user) }, { throwable -> onUserFailure(throwable) })
            }
        }

        lifecycleScope.launch {
            viewModel.avatarFlow.collectLatest { either ->
                either.fold(::onAvatarSuccess, ::onAvatarFailure)
                fragment_user_avatar.visibility = View.VISIBLE
                fragment_user_avatar_progress.visibility = View.GONE
            }
        }

        lifecycleScope.launch {
            viewModel.logoffFlow.collectLatest {
                navigation.back()
            }
        }

        when (val account = arguments.account) {
            is UserAccount.Me -> {
                fragment_user_toolbar.title = account.login
                fragment_user_toolbar.menu.setGroupVisible(R.id.menu_user_options_group_me, true)
            }
            is UserAccount.User -> {
                fragment_user_toolbar.title = account.login
            }
        }
    }

    private fun onMenuItemClick(item: MenuItem) = when (item.itemId) {
        R.id.menu_user_options_action_logout -> true.apply {
            lifecycleScope.launch { viewModel.logoffChannel.send(Unit) }
        }
        else -> false
    }

    private fun onUserSuccess(user: User) {
        fragment_user_content.visibility = View.VISIBLE
        fragment_user_toolbar.title = user.login
        fragment_user_fullname.text = user.fullname
        fragment_user_registered.text = SimpleDateFormat("MMMM dd, YYYY").format(user.timeRegistered)
        fragment_user_speciality.text = user.speciality

        fragment_user_counters_karma.text = user.score.toString()
        fragment_user_counters_rating.text = user.rating.toString()
        fragment_user_counters_followers.text = user.followersCount.toString()
        fragment_user_counters_following.text = user.followsCount.toString()
    }

    private fun onUserFailure(throwable: Throwable) {
        fragment_user_avatar_progress.visibility = View.GONE
        exceptionController.render(exceptionHandler.handleException(throwable))
        fragment_user_exception.findViewById<Button>(R.id.layout_exception_retry).visibility = View.GONE
    }

    private fun onAvatarSuccess(response: GetContentResponse) {
        val drawable = response.bytes.toRoundedDrawable(resources, dp2px(R.dimen.radiusS))
        fragment_user_avatar.setImageDrawable(drawable)
    }

    private fun onAvatarFailure(throwable: Throwable) {
        fragment_user_avatar.setImageResource(R.drawable.ic_account_stub)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(VIEW_MODEL_STATE_KEY, viewModel.toString())
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