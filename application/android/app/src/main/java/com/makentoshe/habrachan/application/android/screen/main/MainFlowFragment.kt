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
        R.id.action_account -> onAccountClick()
        R.id.action_articles -> onArticlesClick()
        R.id.action_menu -> onMenuClick()
        else -> false
    }

    private fun onAccountClick(): Boolean {
        navigator.navigateToAccountScreen()
        return true
    }

    private fun onArticlesClick(): Boolean {
        navigator.navigateToArticlesFlowScreen()
        return true
    }

    private fun onMenuClick(): Boolean {
        navigator.navigateToMenuScreen()
        return true
    }

    class Arguments(fragment: MainFlowFragment): CoreFragment.Arguments(fragment) {

        var page: Int
            get() = fragmentArguments.getInt(PAGE, 1)
            set(value) = fragmentArguments.putInt(PAGE, value)

        companion object {
            private const val PAGE = "Page"
        }

    }
}

