package com.makentoshe.habrachan.view.user

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.getResourceIdOrThrow
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.broadcast.LoginBroadcastReceiver
import com.makentoshe.habrachan.common.broadcast.LogoutBroadcastReceiver
import com.makentoshe.habrachan.common.entity.User
import com.makentoshe.habrachan.common.navigation.Router
import com.makentoshe.habrachan.common.network.request.ImageRequest
import com.makentoshe.habrachan.common.network.response.ImageResponse
import com.makentoshe.habrachan.common.network.response.UserResponse
import com.makentoshe.habrachan.common.ui.ImageViewController
import com.makentoshe.habrachan.model.user.UserAccount
import com.makentoshe.habrachan.model.user.UserContentPagerAdapter
import com.makentoshe.habrachan.ui.user.UserFragmentUi
import com.makentoshe.habrachan.view.main.MainFlowFragment
import com.makentoshe.habrachan.viewmodel.article.UserAvatarViewModel
import com.makentoshe.habrachan.viewmodel.user.UserViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import toothpick.ktp.delegate.inject

class UserFragment : Fragment() {

    private val disposables = CompositeDisposable()
    private val arguments = Arguments(this)
    private val navigator by inject<Navigator>()
    private val viewModel by inject<UserViewModel>()
    private val userAvatarViewModel by inject<UserAvatarViewModel>()

    private lateinit var toolbarView: Toolbar
    private lateinit var messageView: TextView
    private lateinit var retryButton: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return UserFragmentUi().create(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        progressBar = view.findViewById(R.id.user_fragment_progress)
        retryButton = view.findViewById(R.id.user_fragment_retry)
        messageView = view.findViewById(R.id.user_fragment_message)
        toolbarView = view.findViewById(R.id.user_fragment_toolbar)

        setRetryButtonBehavior()
        setToolbarBehavior()

        viewModel.userObservable.observeOn(AndroidSchedulers.mainThread())
            .subscribe(::onUserResponse).let(disposables::add)

        userAvatarViewModel.avatarObservable.subscribe(::onAvatarResponse).let(disposables::add)

        if (savedInstanceState == null) {
            viewModel.userObserver.onNext(arguments.userAccount)
        }
    }

    private fun setRetryButtonBehavior() = retryButton.setOnClickListener {
        viewModel.userObserver.onNext(arguments.userAccount)
        progressBar.visibility = View.VISIBLE
        messageView.visibility = View.GONE
        retryButton.visibility = View.GONE
    }

    private fun setToolbarBehavior() {
        toolbarView.setNavigationOnClickListener { navigator.back() }
        toolbarView.setOnMenuItemClickListener(::onUserOptionsMenuClick)
        toolbarView.overflowIcon = buildToolbarOverflowIcon()
        if (arguments.userAccount != UserAccount.Me) {
            toolbarView.setNavigationIcon(R.drawable.ic_arrow_back)
            toolbarView.menu.setGroupVisible(R.id.group_user_custom, true)
            toolbarView.menu.setGroupVisible(R.id.group_user_me, false)
        } else {
            toolbarView.menu.setGroupVisible(R.id.group_user_custom, false)
            toolbarView.menu.setGroupVisible(R.id.group_user_me, true)
        }
    }

    @SuppressLint("NewApi")
    private fun buildToolbarOverflowIcon(): Drawable {
        val toolbarOverflowIcon = resources.getDrawable(R.drawable.ic_overflow, requireContext().theme)
        val styleTypedArray = requireContext().theme.obtainStyledAttributes(intArrayOf(R.attr.habrachanToolbar))
        val styleResource = styleTypedArray.getResourceIdOrThrow(0)
        val colorTypedArray = requireContext().obtainStyledAttributes(styleResource, intArrayOf(android.R.attr.tint))
        val color = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M/*23*/) {
            @Suppress("DEPRECATION")
            resources.getColor(colorTypedArray.getResourceIdOrThrow(0))
        } else {
            resources.getColor(colorTypedArray.getResourceIdOrThrow(0), requireContext().theme)
        }
        colorTypedArray.recycle()
        styleTypedArray.recycle()
        return toolbarOverflowIcon.apply { setTint(color) }
    }

    private fun onUserOptionsMenuClick(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_logout -> onUserLogout()
        else -> false
    }

    private fun onUserLogout(): Boolean {
        LogoutBroadcastReceiver.send(requireContext())
        return true
    }

    private fun onUserResponse(response: UserResponse) = when (response) {
        is UserResponse.Success -> onUserSuccess(response)
        is UserResponse.Error -> onUserError(response)
    }

    @SuppressLint("RestrictedApi")
    private fun onUserSuccess(response: UserResponse.Success) {
        if (arguments.userAccount == UserAccount.Me) {
            LoginBroadcastReceiver.send(requireContext(), response.user.login)
        }

        userAvatarViewModel.avatarObserver.onNext(ImageRequest(response.user.avatar))

        val view = view ?: return Toast.makeText(requireContext(), "View does not responds", Toast.LENGTH_LONG).show()

        view.findViewById<View>(R.id.user_fragment_body).visibility = View.VISIBLE

        toolbarView.title = response.user.login

        val fullNameView = view.findViewById<TextView>(R.id.user_fragment_fullname_text)
        fullNameView.text = response.user.fullname

        val karmaView = view.findViewById<TextView>(R.id.user_fragment_karma_value)
        karmaView.text = response.user.score.toString()

        val ratingView = view.findViewById<TextView>(R.id.user_fragment_rating_value)
        ratingView.text = response.user.rating.toString()

        val specializmView = view.findViewById<TextView>(R.id.user_fragment_specializm)
        specializmView.text = response.user.specializm

        progressBar.visibility = View.GONE

        setUserContent(response)
    }

    private fun setUserContent(response: UserResponse.Success) {
        val view = view ?: return

        val count = if (arguments.userAccount == UserAccount.Me) 4 else 3
        val adapter = UserContentPagerAdapter(this, response.user, count)

        val viewPager = view.findViewById<ViewPager2>(R.id.user_fragment_viewpager)
        viewPager.adapter = adapter

        val tabLayout = view.findViewById<TabLayout>(R.id.user_fragment_tab)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = resources.getStringArray(R.array.user_fragment_tabs)[position]
        }.attach()
    }

    private fun onUserError(response: UserResponse.Error) {
        progressBar.visibility = View.GONE
        messageView.visibility = View.VISIBLE
        messageView.text = response.message
        retryButton.visibility = View.VISIBLE
    }

    private fun onAvatarResponse(response: ImageResponse) = when (response) {
        is ImageResponse.Success -> onAvatarSuccess(response)
        is ImageResponse.Error -> onAvatarError(response)
    }

    private fun onAvatarSuccess(response: ImageResponse.Success) {
        val avatarView = requireView().findViewById<ImageView>(R.id.user_fragment_avatar)
        if (response.isStub) {
            ImageViewController(avatarView).setAvatarStub()
        } else {
            ImageViewController(avatarView).setAvatarFromByteArray(response.bytes)
        }
    }

    private fun onAvatarError(response: ImageResponse.Error) {
        val avatarView = requireView().findViewById<ImageView>(R.id.user_fragment_avatar)
        ImageViewController(avatarView).setAvatarStub()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    class Factory {

        fun build(userAccount: UserAccount): UserFragment {
            val fragment = UserFragment()
            fragment.arguments.userAccount = userAccount
            return fragment
        }
    }

    class Arguments(private val userFragment: UserFragment) {

        init {
            val fragment = userFragment as Fragment
            if (fragment.arguments == null) {
                fragment.arguments = Bundle()
            }
        }

        private val fragmentArguments: Bundle
            get() = userFragment.requireArguments()

        var userAccount: UserAccount
            get() = fragmentArguments.get(USERACCOUNT) as UserAccount
            set(value) = fragmentArguments.putSerializable(USERACCOUNT, value)

        companion object {
            private const val USERACCOUNT = "UserAccount"
        }

    }

    class Navigator(private val router: Router) {
        fun back() = router.exit()
    }
}
