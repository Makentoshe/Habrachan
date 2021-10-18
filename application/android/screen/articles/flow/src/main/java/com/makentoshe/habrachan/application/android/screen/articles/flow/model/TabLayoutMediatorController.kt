package com.makentoshe.habrachan.application.android.screen.articles.flow.model

import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class TabLayoutMediatorController(
    private val articlesUserSearches: List<ArticlesUserSearch>,
) {

    fun attach(tabLayout: TabLayout, viewPager2: ViewPager2) {
        TabLayoutMediator(tabLayout, viewPager2) { tab, position -> strategy(tab, position) }.attach()
    }

    fun strategy(tab: TabLayout.Tab, position: Int) {
        tab.text = articlesUserSearches[position].title
    }
}