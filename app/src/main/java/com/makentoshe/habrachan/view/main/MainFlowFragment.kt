package com.makentoshe.habrachan.view.main

import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.common.broadcast.LoginBroadcastReceiver
import com.makentoshe.habrachan.common.broadcast.LogoutBroadcastReceiver
import com.makentoshe.habrachan.common.database.session.SessionDatabase
import com.makentoshe.habrachan.model.main.MainFlowPagerAdapter
import com.makentoshe.habrachan.ui.main.MainFlowFragmentUi
import io.reactivex.disposables.CompositeDisposable
import toothpick.ktp.delegate.inject

class MainFlowFragment : Fragment() {

    private val disposables = CompositeDisposable()
    private val arguments = Arguments(this)

    private val sessionDatabase by inject<SessionDatabase>()
    private val logoutBroadcastReceiver by inject<LogoutBroadcastReceiver>()
    private val loginBroadcastReceiver by inject<LoginBroadcastReceiver>()

    private lateinit var viewPager: ViewPager2
    private lateinit var bottomNavigation: TabLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return MainFlowFragmentUi().inflateView(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewPager = view.findViewById(R.id.main_view_pager)
        bottomNavigation = view.findViewById(R.id.main_bottom_navigation)

        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewPager.adapter = MainFlowPagerAdapter(this)
        viewPager.isUserInputEnabled = false
        if (savedInstanceState == null) viewPager.post {
            viewPager.setCurrentItem(1, false)
            viewPager.visibility = View.VISIBLE
        } else {
            viewPager.visibility = View.VISIBLE
        }

        val configuredTabs = (0 until bottomNavigation.tabCount).map { position ->
            val tab = bottomNavigation.getTabAt(position)
            return@map tab?.text to tab?.icon
        }
        TabLayoutMediator(bottomNavigation, viewPager) { tab, position ->
            tab.text = configuredTabs[position].first.toString()
            tab.icon = configuredTabs[position].second
        }.attach()

        if (sessionDatabase.me().isEmpty) setEmptyAccount() else setLoggedAccount()

        logoutBroadcastReceiver.observable.subscribe {
            setEmptyAccount()
        }.let(disposables::add)

        loginBroadcastReceiver.observable.subscribe {
            setLoggedAccountAlt(it)
        }.let(disposables::add)
    }

    private fun setEmptyAccount() {
        val tab = bottomNavigation.getTabAt(0)
        tab?.setText(R.string.menu_account)
        tab?.setIcon(R.drawable.ic_account_outline)
    }

    private fun setLoggedAccount() {
        setLoggedAccountAlt(sessionDatabase.me().get().login)
    }

    private fun setLoggedAccountAlt(login: String) {
        val tab = bottomNavigation.getTabAt(0)
        tab?.text = login
        tab?.setIcon(R.drawable.ic_account)
    }

    override fun onStart() {
        super.onStart()

        val logoutIntentFilter = IntentFilter(LogoutBroadcastReceiver.ACTION)
        requireContext().registerReceiver(logoutBroadcastReceiver, logoutIntentFilter)

        val loginIntentFilter = IntentFilter(LoginBroadcastReceiver.ACTION)
        requireContext().registerReceiver(loginBroadcastReceiver, loginIntentFilter)
    }

    override fun onStop() {
        super.onStop()
        requireContext().unregisterReceiver(logoutBroadcastReceiver)
        requireContext().unregisterReceiver(loginBroadcastReceiver)
    }

    class Arguments(private val mainFlowFragment: MainFlowFragment) {

        init {
            val fragment = mainFlowFragment as Fragment
            if (fragment.arguments == null) {
                fragment.arguments = Bundle()
            }
        }

        private val fragmentArguments: Bundle
            get() = mainFlowFragment.requireArguments()

        var page: Int
            get() = fragmentArguments.getInt(PAGE, 1)
            set(value) = fragmentArguments.putInt(PAGE, value)

        companion object {
            private const val PAGE = "Page"
        }

    }
}

