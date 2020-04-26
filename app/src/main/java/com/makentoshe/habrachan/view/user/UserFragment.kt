package com.makentoshe.habrachan.view.user

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.getResourceIdOrThrow
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.makentoshe.habrachan.R
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
    private val flowNavigator by inject<MainFlowFragment.Navigator>()
    private val viewModel by inject<UserViewModel>()
    private val userAvatarViewModel by inject<UserAvatarViewModel>()

    private lateinit var toolbarView: Toolbar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return UserFragmentUi().create(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        toolbarView = view.findViewById(R.id.user_fragment_toolbar)
        toolbarView.setNavigationOnClickListener { navigator.back() }
        toolbarView.setOnMenuItemClickListener(::onUserOptionsMenuClick)
        toolbarView.overflowIcon = buildToolbarOverflowIcon(toolbarView)
        if (arguments.userAccount != UserAccount.Me) {
            toolbarView.setNavigationIcon(R.drawable.ic_arrow_back)
            toolbarView.menu.setGroupVisible(R.id.group_user_custom, true)
            toolbarView.menu.setGroupVisible(R.id.group_user_me, false)
        } else {
            toolbarView.menu.setGroupVisible(R.id.group_user_custom, false)
            toolbarView.menu.setGroupVisible(R.id.group_user_me, true)
        }

        viewModel.userObservable.observeOn(AndroidSchedulers.mainThread())
            .subscribe(::onUserResponse).let(disposables::add)

        userAvatarViewModel.avatarObservable.subscribe(::onAvatarResponse).let(disposables::add)

        if (savedInstanceState == null) {
            viewModel.userObserver.onNext(arguments.userAccount)
        }
    }

    private fun buildToolbarOverflowIcon(toolbar: Toolbar): Drawable {
        val toolbarOverflowIcon = resources.getDrawable(R.drawable.ic_overflow, requireContext().theme)
        val styleTypedArray = requireContext().theme.obtainStyledAttributes(intArrayOf(R.attr.habrachanToolbar))
        val styleResource = styleTypedArray.getResourceIdOrThrow(0)
        val colorTypedArray = requireContext().obtainStyledAttributes(styleResource, intArrayOf(android.R.attr.tint))
        val color = resources.getColor(colorTypedArray.getResourceIdOrThrow(0), requireContext().theme)
        colorTypedArray.recycle()
        styleTypedArray.recycle()
        return toolbarOverflowIcon.apply { setTint(color) }
    }

    private fun onUserOptionsMenuClick(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_logout -> onUserLogout()
        else -> false
    }

    private fun onUserLogout(): Boolean {
        clearUserLoginInBottomNavigationBar()
        viewModel.userLogoutObserver.onComplete()
        flowNavigator.toLoginScreen()
        return true
    }

    private fun onUserResponse(response: UserResponse) = when (response) {
        is UserResponse.Success -> onUserSuccess(response)
        is UserResponse.Error -> onUserError(response)
    }

    @SuppressLint("RestrictedApi")
    private fun onUserSuccess(response: UserResponse.Success) {
        if (arguments.userAccount == UserAccount.Me) {
            setUserLoginInBottomNavigationBar(response.user)
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

        val progressBar = view.findViewById<ProgressBar>(R.id.user_fragment_progress)
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
        val decorView = view ?: activity?.window?.decorView!!
        Snackbar.make(decorView, response.toString(), Snackbar.LENGTH_LONG).show()
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

    private fun setUserLoginInBottomNavigationBar(user: User) {
        val navigation = requireActivity().findViewById<BottomNavigationView>(R.id.main_bottom_navigation) ?: return
        val item = navigation.menu.findItem(R.id.action_account)
        item.setIcon(R.drawable.ic_account)
        item.title = user.login
    }

    private fun clearUserLoginInBottomNavigationBar() {
        val navigation = requireActivity().findViewById<BottomNavigationView>(R.id.main_bottom_navigation) ?: return
        val item = navigation.menu.findItem(R.id.action_account)
        item.setIcon(R.drawable.ic_account_outline)
        item.setTitle(R.string.menu_account)
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
