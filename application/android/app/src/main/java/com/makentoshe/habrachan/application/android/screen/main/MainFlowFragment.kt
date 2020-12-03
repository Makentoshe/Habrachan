package com.makentoshe.habrachan.application.android.screen.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.makentoshe.habrachan.R
import com.makentoshe.habrachan.application.android.CoreFragment
import com.makentoshe.habrachan.application.android.screen.main.navigation.MainFlowNavigation
import kotlinx.android.synthetic.main.main_flow_fragment.*
import toothpick.ktp.delegate.inject

class MainFlowFragment : CoreFragment() {

    override val arguments = Arguments(this)

    private val navigator by inject<MainFlowNavigation>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.main_flow_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fragment_main_navigation.setOnNavigationItemSelectedListener(::onNavigationItemSelected)
        fragment_main_navigation.selectedItemId = R.id.action_articles
//        fragment_main_navigation.visibility = View.GONE
    }

    private fun onNavigationItemSelected(item: MenuItem) = when (item.itemId) {
//        R.id.action_account -> onAccountClick()
        R.id.action_articles -> onArticlesClick()
//        R.id.action_menu -> onMenuClick()
        else -> false
    }

    private fun onAccountClick(): Boolean {
        navigator.navigateToAccountScreen()
        return true
    }

    private fun onArticlesClick(): Boolean {
        navigator.navigateToArticlesScreen(arguments.page)
        return true
    }

    private fun onMenuClick(): Boolean {
        navigator.navigateToMenuScreen()
        return true
    }

//    private fun setEmptyAccount(item: MenuItem) {
//        item.setTitle(R.string.menu_account)
//        item.setIcon(R.drawable.ic_account_outline)
//    }
//
//    private fun setLoggedAccount(item: MenuItem) {
//        setLoggedAccountAlt(item, sessionDatabase.me().get().login)
//    }
//
//    private fun setLoggedAccountAlt(item: MenuItem, login: String) {
//        item.title = login
//        item.setIcon(R.drawable.ic_account)
//    }
//
//    override fun onStart() {
//        super.onStart()
//        val logoutIntentFilter = IntentFilter(LogoutBroadcastReceiver.ACTION)
//        requireContext().registerReceiver(logoutBroadcastReceiver, logoutIntentFilter)
//        val loginIntentFilter = IntentFilter(LoginBroadcastReceiver.ACTION)
//        requireContext().registerReceiver(loginBroadcastReceiver, loginIntentFilter)
//    }
//
//    override fun onStop() {
//        super.onStop()
//        requireContext().unregisterReceiver(logoutBroadcastReceiver)
//        requireContext().unregisterReceiver(loginBroadcastReceiver)
//    }

    class Arguments(fragment: MainFlowFragment): CoreFragment.Arguments(fragment) {

        var page: Int
            get() = fragmentArguments.getInt(PAGE, 1)
            set(value) = fragmentArguments.putInt(PAGE, value)

        companion object {
            private const val PAGE = "Page"
        }

    }
}

