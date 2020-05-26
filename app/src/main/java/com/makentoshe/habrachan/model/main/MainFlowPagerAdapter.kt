package com.makentoshe.habrachan.model.main

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.makentoshe.habrachan.navigation.main.articles.ArticlesFlowScreen
import com.makentoshe.habrachan.navigation.main.login.LoginFlowScreen
import com.makentoshe.habrachan.navigation.main.menu.MenuScreen

class MainFlowPagerAdapter(private val fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int) = when (position) {
        0 -> LoginFlowScreen().fragment
        1 -> ArticlesFlowScreen(page = 1).fragment
        2 -> MenuScreen().fragment
        else -> throw IllegalArgumentException("$position")
    }

    override fun getItemCount() = 3
}