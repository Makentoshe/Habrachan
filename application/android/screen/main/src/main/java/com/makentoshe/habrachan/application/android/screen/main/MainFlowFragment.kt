package com.makentoshe.habrachan.application.android.screen.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.makentoshe.habrachan.application.android.CoreFragment
import com.makentoshe.habrachan.application.android.main.R
import com.makentoshe.habrachan.application.android.screen.main.navigation.MainFlowNavigation
import kotlinx.android.synthetic.main.fragment_main.*
import toothpick.ktp.delegate.inject

class MainFlowFragment : CoreFragment() {

    private val navigation by inject<MainFlowNavigation>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fragment_main_navigation.setOnNavigationItemSelectedListener(::onBottomNavigationSelectListener)
        fragment_main_navigation.selectedItemId = R.id.fragment_main_articles_action
    }

    private fun onBottomNavigationSelectListener(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.fragment_main_account_action -> navigation.navigateToAccountScreen()
            R.id.fragment_main_articles_action -> navigation.navigateToArticlesScreen()
            R.id.fragment_main_menu_action -> navigation.navigateToMenuScreen()
            else -> return false
        }
        return true
    }

}
